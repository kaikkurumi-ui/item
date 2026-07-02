package com.aisia.item.module.generate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import org.apache.ibatis.type.JdbcType;

import java.util.Collections;
import java.util.Map;

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create(
                        "jdbc:mysql://localhost:3306/item?allowPublicKeyRetrieval=true&characterEncoding=utf8&serverTimezone=UTC",
                        "root",
                        "123456"
                ).dataSourceConfig(builder -> {
                    builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                                if (JdbcType.TINYINT == metaInfo.getJdbcType()) {
                                    return DbColumnType.INTEGER;
                                }
                                return typeRegistry.getColumnType(metaInfo);
                            })
                            .keyWordsHandler(new MySqlKeyWordsHandler())
                            .schema("item");;
                }).globalConfig(builder -> {
                    builder.disableOpenDir()
                            .disableServiceInterface()
                            .outputDir(System.getProperty("user.dir") + "/module/src/main/java")
                            .author("kaikai")
                            .dateType(DateType.ONLY_DATE)
                            .commentDate("yyyy-MM-dd");
                }).packageConfig(builder -> {
                    builder.parent("com.aisia.item.module")
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service")
                            .mapper("mapper")
                            .xml("mapper")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/module/src/main/resources/mapper"));
                }).strategyConfig(builder -> {
                    builder.addInclude("test")
                            .entityBuilder()
                            .enableLombok() // 启用 Lombok
                            .enableTableFieldAnnotation()
                            .javaTemplate("/templates/customEntity.java")
                            .disableSerialVersionUID()
                            .enableLombok()
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            .idType(IdType.AUTO)
                            .formatFileName("%sEntity")

                            .controllerBuilder()
                            .disable()
//                    .enableHyphenStyle()
//                    .enableRestStyle()
//                    .formatFileName("%sController")
//                    .template("/templates/customController.java")

                            .serviceBuilder()
                            .disableService() //不生成IService接口
                            .formatServiceImplFileName("%sService")
                            .serviceImplTemplate("/templates/customService.java")

                            .mapperBuilder()
                            .mapperTemplate("/templates/customMapper.java")// ← 自定义 Mapper 模板
                            .mapperXmlTemplate("/templates/customMapper.xml")// ← 自定义 Mapper.xml 模板
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sXml")
                            .build();
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
