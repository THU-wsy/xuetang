package com.thuwsy.xuetang.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thuwsy.xuetang.base.exception.XueTangException;
import com.thuwsy.xuetang.content.dto.SaveTeachplanDto;
import com.thuwsy.xuetang.content.dto.TeachplanDto;
import com.thuwsy.xuetang.content.mapper.TeachplanMediaMapper;
import com.thuwsy.xuetang.content.po.Teachplan;
import com.thuwsy.xuetang.content.po.TeachplanMedia;
import com.thuwsy.xuetang.content.service.TeachplanService;
import com.thuwsy.xuetang.content.mapper.TeachplanMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 吴晟宇
* @description 针对表【teachplan(课程计划)】的数据库操作Service实现
* @createDate 2024-01-12 14:44:11
*/
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan>
    implements TeachplanService{

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(Long courseId) {
        // 1. 查询出该课程的所有大章节和小章节
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teachplan::getCourseId, courseId);
        List<Teachplan> teachplans = teachplanMapper.selectList(wrapper);

        // 2. 将Teachplan映射为TeachplanDto
        List<TeachplanDto> plans = teachplans.stream().map(teachplan -> {
            TeachplanDto teachplanDto = new TeachplanDto();
            BeanUtils.copyProperties(teachplan, teachplanDto);
            return teachplanDto;
        }).collect(Collectors.toList());

        // 3. 查询出该课程关联的所有媒资信息
        LambdaQueryWrapper<TeachplanMedia> mediaWrapper = new LambdaQueryWrapper<>();
        mediaWrapper.eq(TeachplanMedia::getCourseId, courseId);
        List<TeachplanMedia> medias = teachplanMediaMapper.selectList(mediaWrapper);

        // 4. 找到所有大章节
        List<TeachplanDto> list = plans.stream().filter(entity ->
            // 父节点id为0的即为大章节
            entity.getParentid().equals(0L)
        ).map(entity -> {
            // 设置大章节的所有子章节
            entity.setTeachPlanTreeNodes(getChildren(entity, plans, medias));
            return entity;
        }).sorted((entity1, entity2) -> {
            // 进行排序
            int a = entity1.getOrderby() == null ? 0 : entity1.getOrderby();
            int b = entity2.getOrderby() == null ? 0 : entity2.getOrderby();
            return a - b;
        }).collect(Collectors.toList());

        return list.size() == 0 ? null : list;
    }

    /**
     * 获取大章节current的所有子章节
     */
    private List<TeachplanDto> getChildren(TeachplanDto current,
            List<TeachplanDto> plans, List<TeachplanMedia> medias) {
        List<TeachplanDto> list = plans.stream().filter(entity ->
            // 父节点id等于current的id的节点即为current的子节点
            entity.getParentid().equals(current.getId())
        ).map(entity -> {
            // 给这些子节点设置对应的媒资信息
            TeachplanMedia teachplanMedia = null;
            for (TeachplanMedia media : medias) {
                if (media.getTeachplanId().equals(entity.getId())) {
                    teachplanMedia = media;
                    break;
                }
            }
            entity.setTeachplanMedia(teachplanMedia);
            return entity;
        }).sorted((entity1, entity2) -> {
            // 进行排序
            int a = entity1.getOrderby() == null ? 0 : entity1.getOrderby();
            int b = entity2.getOrderby() == null ? 0 : entity2.getOrderby();
            return a - b;
        }).collect(Collectors.toList());

        return list.size() == 0 ? null : list;
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto dto) {
        Long id = dto.getId();

        // id不为空，则进行修改
        if (id != null) {
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(dto, teachplan);
            // 1. 设置修改日期
            teachplan.setChangeDate(LocalDateTime.now());
            // 2. 进行修改
            teachplanMapper.updateById(teachplan);
            return;
        }

        // id为空，则进行新增
        // 1. 首先获取同一课程，父节点相同的所有教学计划的排序字段
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teachplan::getCourseId, dto.getCourseId())
                .eq(Teachplan::getParentid, dto.getParentid())
                .select(Teachplan::getOrderby);
        List<Teachplan> list = teachplanMapper.selectList(wrapper);

        // 2. 然后获取排序字段的最大值
        int max = 0;
        for (Teachplan plan : list) {
            if (plan.getOrderby() >= max)
                max = plan.getOrderby();
        }

        Teachplan teachplan = new Teachplan();
        BeanUtils.copyProperties(dto, teachplan);
        // 3. 设置排序号
        teachplan.setOrderby(max + 1);
        // 4. 设置创建日期
        teachplan.setCreateDate(LocalDateTime.now());
        // 5. 进行新增
        teachplanMapper.insert(teachplan);
    }

    @Transactional
    @Override
    public void deleteTeachplan(Long id) {
        // 1. 判断当前章节是否有子章节
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teachplan::getParentid, id);
        Long count = teachplanMapper.selectCount(wrapper);

        if (count > 0) {
            // 2. 有子章节，则抛出异常
            XueTangException.cast("课程计划信息还有子级信息，无法操作");
        } else {
            // 3. 没有子章节，则直接删除该课程计划和对应的媒资信息
            teachplanMapper.deleteById(id);

            LambdaQueryWrapper<TeachplanMedia> mediaWrapper = new LambdaQueryWrapper<>();
            mediaWrapper.eq(TeachplanMedia::getTeachplanId, id);
            teachplanMediaMapper.delete(mediaWrapper);
        }
    }

    @Transactional
    @Override
    public void moveDownTeachplan(Long id) {
        // 1. 先查询到该章节对应的信息
        Teachplan teachplan = teachplanMapper.selectById(id);

        // 2. 查询同一级别的下一个章节信息
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<>();
        // 课程id相同，且父节点id相同，则属于同一级别
        // 然后搜索排序字段大于当前章节的，即为下一个章节
        // 如果没有，则返回null，表示当前章节已经是最后一个
        wrapper.eq(Teachplan::getCourseId, teachplan.getCourseId())
                .eq(Teachplan::getParentid, teachplan.getParentid())
                .gt(Teachplan::getOrderby, teachplan.getOrderby())
                .orderByAsc(Teachplan::getOrderby)
                .last("LIMIT 1");
        Teachplan tmp = teachplanMapper.selectOne(wrapper);

        if (tmp == null) return;

        // 3. 如果有下一个章节，则交换这两个章节的排序字段
        int order = tmp.getOrderby();
        tmp.setOrderby(teachplan.getOrderby());
        teachplan.setOrderby(order);

        teachplanMapper.updateById(teachplan);
        teachplanMapper.updateById(tmp);
    }

    @Transactional
    @Override
    public void moveUpTeachplan(Long id) {
        // 1. 先查询到该章节对应的信息
        Teachplan teachplan = teachplanMapper.selectById(id);

        // 2. 查询同一级别的上一个章节信息
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<>();
        // 课程id相同，且父节点id相同，则属于同一级别
        // 然后搜索排序字段小于当前章节的，即为上一个章节
        // 如果没有，则返回null，表示当前章节已经是第一个
        wrapper.eq(Teachplan::getCourseId, teachplan.getCourseId())
                .eq(Teachplan::getParentid, teachplan.getParentid())
                .lt(Teachplan::getOrderby, teachplan.getOrderby())
                .orderByDesc(Teachplan::getOrderby)
                .last("LIMIT 1");
        Teachplan tmp = teachplanMapper.selectOne(wrapper);

        if (tmp == null) return;

        // 3. 如果有上一个章节，则交换这两个章节的排序字段
        int order = tmp.getOrderby();
        tmp.setOrderby(teachplan.getOrderby());
        teachplan.setOrderby(order);

        teachplanMapper.updateById(teachplan);
        teachplanMapper.updateById(tmp);
    }
}




