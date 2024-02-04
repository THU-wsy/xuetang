package com.thuwsy.xuetang.checkcode.service.impl;

import com.baomidou.mybatisplus.core.toolkit.EncryptUtils;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.thuwsy.xuetang.base.utils.EncryptUtil;
import com.thuwsy.xuetang.checkcode.dto.CheckCodeParamsDto;
import com.thuwsy.xuetang.checkcode.dto.CheckCodeResultDto;
import com.thuwsy.xuetang.checkcode.service.AbstractCheckCodeService;
import com.thuwsy.xuetang.checkcode.service.CheckCodeService;
import io.netty.handler.codec.base64.Base64Encoder;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * ClassName: PicCheckCodeServiceImpl
 * Package: com.thuwsy.xuetang.checkcode.service.impl
 * Description: 图片验证码生成器
 *
 * @Author THU_wsy
 * @Create 2024/1/31 12:54
 * @Version 1.0
 */
@Service("PicCheckCodeService")
public class PicCheckCodeServiceImpl extends AbstractCheckCodeService implements CheckCodeService {

    @Autowired
    private DefaultKaptcha kaptcha;

    @Resource(name="NumberLetterCheckCodeGenerator")
    @Override
    public void setCheckCodeGenerator(CheckCodeGenerator checkCodeGenerator) {
        this.checkCodeGenerator = checkCodeGenerator;
    }

    @Resource(name="UUIDKeyGenerator")
    @Override
    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }


    @Resource(name="RedisCheckCodeStore")
    @Override
    public void setCheckCodeStore(CheckCodeStore checkCodeStore) {
        this.checkCodeStore = checkCodeStore;
    }


    @Override
    public CheckCodeResultDto generate(CheckCodeParamsDto checkCodeParamsDto) {
        GenerateResult generate = generate(checkCodeParamsDto, 4, "checkcode:", 60);
        String key = generate.getKey();
        String code = generate.getCode();
        String pic = createPic(code);
        CheckCodeResultDto checkCodeResultDto = new CheckCodeResultDto();
        checkCodeResultDto.setAliasing(pic);
        checkCodeResultDto.setKey(key);
        return checkCodeResultDto;

    }

    private String createPic(String code) {
        // 生成图片验证码
        ByteArrayOutputStream outputStream = null;
        BufferedImage image = kaptcha.createImage(code);

        outputStream = new ByteArrayOutputStream();
        String imgBase64Encoder = null;
        try {
            // 对字节数组Base64编码
            Base64Encoder base64Encoder = new Base64Encoder();
            ImageIO.write(image, "png", outputStream);
            imgBase64Encoder = "data:image/png;base64," + EncryptUtil.encodeBase64(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imgBase64Encoder;
    }
}
