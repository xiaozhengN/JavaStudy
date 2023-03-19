package com.atguigu.java8;

public class SubClass /*extends MyClass*/ implements com.atguigu.java8.MyFun, com.atguigu.java8.MyInterface {

	@Override
	public String getName() {
		return com.atguigu.java8.MyInterface.super.getName();
	}

}
