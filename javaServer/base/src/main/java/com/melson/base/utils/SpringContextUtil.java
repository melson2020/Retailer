package com.melson.base.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

	// Spring
	private static ApplicationContext applicationContext;

	/**
	 * ʵ��ApplicationContextAware�ӿڵĻص����������������Ļ���
	 * 
	 * @param applicationContext
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextUtil.applicationContext = applicationContext;
	}

	/**
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * ��ȡ���� ������д��bean����������Ҫ����
	 * 
	 * @param name
	 * @return Object һ�����������ע���bean��ʵ��
	 * @throws BeansException
	 */
	public static <T> T getBean(String name, Class<T> cls) throws BeansException {
		return applicationContext.getBean(name, cls);
	}

	/**
	 * ��ȡ���� ������д��bean����������Ҫ����
	 *
	 * @param cls
	 *            ����
	 * @return Object һ�����������ע���bean��ʵ��
	 * @throws BeansException
	 */
	public static <T> T getBean(Class<T> cls) throws BeansException {
		return applicationContext.getBean(cls);
	}

}
