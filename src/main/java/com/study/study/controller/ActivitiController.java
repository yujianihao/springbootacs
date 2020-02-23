package com.study.study.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/activiti")
public class ActivitiController {
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RepositoryService  repositoryService;

	@RequestMapping(value = "/start")
	public String initStart() {
		System.out.println("method startActivityDemo begin....");
		System.out.println("调用流程存储服务，已部署流程数量:");
		
		Map<String, Object> map = new HashMap<String, Object>();
		identityService.setAuthenticatedUserId("1");
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("apply_vacation", map);
		System.out.println("启动流程成功：" + processInstance.getId());
		
		TaskQuery taskQuery = taskService.createTaskQuery();
		Task singleResult = taskQuery.processInstanceId(processInstance.getId()).singleResult();
		
		System.out.println("任务id：" + singleResult.getId());
		System.out.println("任务的办理人: "+ singleResult.getAssignee());
        System.out.println("任务名称: "+ singleResult.getName());
        System.out.println("任务的创建时间: "+ singleResult.getCreateTime());
        System.out.println("流程实例ID: "+ singleResult.getProcessInstanceId());
        
        Map<String, Object> map2 = new HashMap<String, Object>();
        //map2.put(key, value)
        taskService.complete(singleResult.getId());
        System.out.println("task第一个节点已完成:" + singleResult.getId());
        
        return "success";
	}
	
	
	@RequestMapping(value = "/complete")
	public String completeTask() {
		String processInstanceId = "2506";
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		taskService.complete(task.getId());
		return "success";
	}
	
	
	 /**
     * 获取流程图 执行到哪里高亮显示
     * @param procDefId 部署的流程id  在 act_re_procdef 这张表里
     * @param execId  要查询的流程执行的id（开启了一个流程就会生成一条执行的数据）  在 act_ru_execution 这张表里（该表下PROC_DEF_ID_字段可以判断哪个流程）
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getActPic")
    public void  getActPic(HttpServletResponse response)throws Exception {
    	Task task = taskService.createTaskQuery().processInstanceId("2506").singleResult();
        InputStream inputStream = currentProcessInstanceImage(task.getId());
        OutputStream outputStream = response.getOutputStream();
     // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = inputStream.read(b, 0, 1024)) != -1) {
        	outputStream.write(b, 0, len);
        }
    }
    
    
    /**
     * 获取当前任务流程图
     *
     * @param taskId
     * @return
     */
    public InputStream currentProcessInstanceImage(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(task.getProcessDefinitionId());
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        // ID 为 流程定义Key
        org.activiti.bpmn.model.Process process = bpmnModel.getProcessById(processDefinition.getKey());
        //UserTask userTask = (UserTask) process.getFlowElement(task.getTaskDefinitionKey());
        // 流程节点ID
        FlowElement flowElement = process.getFlowElement(task.getTaskDefinitionKey());
        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        List<String> highLightedActivities = new ArrayList<>();
        highLightedActivities.add(flowElement.getId());

        // 生成图片
        InputStream inputStream = generator.generateDiagram(bpmnModel, "jpg", highLightedActivities, Collections.emptyList(), "宋体", "宋体", "宋体", null, 2.0);
        return inputStream;
    }
}
