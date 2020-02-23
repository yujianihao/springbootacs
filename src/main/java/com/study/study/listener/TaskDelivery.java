package com.study.study.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TaskDelivery implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("test");
	}

}
