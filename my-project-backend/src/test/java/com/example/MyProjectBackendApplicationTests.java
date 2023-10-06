package com.example;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class MyProjectBackendApplicationTests {
//	@Resource
//	StringRedisTemplate template;

//	@Test
//	void contextLoads() {
//		template.opsForValue().set("a","888");
//		System.out.println(template.opsForValue().get("a"));
//
//	}

	//JavaMailSender是专门用于发送邮件的对象，自动配置类已经提供了Bean
	@Autowired
	JavaMailSender sender;

	@Test
	void contextLoads() {
		//SimpleMailMessage是一个比较简易的邮件封装，支持设置一些比较简单内容
		SimpleMailMessage message = new SimpleMailMessage();
		//设置邮件标题
		message.setSubject("【南京信息工程大学教务处】关于近期学校对您的处分决定");
		//设置邮件内容
		message.setText("姚逸童同学您好，经监控和教务巡查发现，您近期存在旷课、迟到、早退、上课刷抖音行为，" +
				"现已通知相关辅导员，请手写5000字书面检讨，并于2023年10月7号前交到辅导员办公室。");
		//设置邮件发送给谁，可以多个，这里就发给你的QQ邮箱
		message.setTo("1476707975@qq.com");
		//邮件发送者，这里要与配置文件中的保持一致
		message.setFrom("18061946436@163.com");
		//OK，万事俱备只欠发送
		sender.send(message);
	}

//	@Test
//	void contextLoads1() {
//		//使用ConnectionFactory来创建连接
//		ConnectionFactory factory = new ConnectionFactory();
//
//		//设定连接信息，基操
//		factory.setHost("localhost");
//		factory.setPort(5672);  //注意这里写5672，是amqp协议端口
//		factory.setUsername("admin");
//		factory.setPassword("admin");
//		factory.setVirtualHost("/");
//
//		//创建连接
//		try(Connection connection = factory.newConnection()){
//
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//	}


}
