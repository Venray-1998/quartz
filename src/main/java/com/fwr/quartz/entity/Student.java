package com.fwr.quartz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Student
 * 
 * @author  fwr
 * @date 2020-11-20 
 */
@Data
@TableName("student")
public class Student {

	@TableId(type = IdType.AUTO)
	private Integer id;

	private String name;

	private String birth;

	private String sex;

}
