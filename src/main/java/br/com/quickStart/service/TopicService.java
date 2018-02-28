package br.com.quickStart.service;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import br.com.quickStart.entities.Topic;
import br.com.quickStart.exception.TopicException;
import br.com.quickStart.rest.request.TopicRequest;

@Service
public class TopicService {
	
	private static Logger logger = Logger.getLogger(TopicService.class);
	
	private List<Topic> topics = new ArrayList<>(Arrays.asList(
			new Topic("spring", "Spring frameWork", "Spring frameWork description", "back-end"),
			new Topic("java", "java para web", "Java core description", "back-end"),
			new Topic("javascript", "java script", "java script para web description", "front-end"),
			new Topic("javascript2", "java script 2", "java script para web description e", "front-end")
			));
	private Boolean deleteTopic(String id) {
		if(topics.removeIf(t -> t.getId().equals(id))) {
			return true;
		}
		return false;
	}
	private List<Topic> getByCategory(String categoria){
		List<Topic> topicList =  new ArrayList<>();
		if(!isNull(categoria)) {			   
			topics.stream().filter(t -> t.getCategoria().equals(categoria)).forEach(t ->{
				Topic topic = new Topic();
				topic.setId(t.getId());
				topic.setNome(t.getNome());
				topic.setDescription(t.getDescription());
				topicList.add(topic);
			});
		}
		else {
			throw new TopicException("Houve um problema ao carregar a categoria: "+ categoria);
		}
		
		return topicList;
		
	}
	private Topic upDateTopicService(TopicRequest request, String id) {
		if(TopicBydId(id) == null) {
			throw new TopicException("Não exixte o id informado para atualizar: " + id);
		}
		else {
			logger.info("Atualizando o topic de id: " + id);
			for(int i = 0; i <= topics.size(); i++) {
				Topic t = topics.get(i);
				if(t.getId().equals(id)) {
					topics.set(i, new Topic(request.getId(), request.getNome(), request.getDescription(), request.getCategoria()));
					return topics.get(i);
				}
			}
			
		}
		return null;
	}
	private Topic TopicBydId(String id) {
		logger.info("buscar topic pelo id: " + id);
		if(isNull(id)) {
			throw new TopicException("Não existe o id informado: " + id);
		}
		return topics.stream().filter(t ->	t.getId().equals(id)).findFirst().orElse(null);
	}
	private Boolean addTopicService(Topic topic) {
		if(TopicBydId(topic.getId()) == null) {
			topics.add(topic);
			return true;
		}
		
		return false;
	}
	public List<Topic> getAll(){
		logger.info("get all topics: " + "url /topics");
		return topics;
	}
	public Topic getTopic(String id) {
		Topic topicById = TopicBydId(id);
		if(topicById != null) {
			return topicById;
		}else {
			return null;
		}
		
	}
	public void addTopic(TopicRequest topicRequeste) {
		if(topicRequeste != null) {
			Boolean add = addTopicService(new Topic(topicRequeste.getId(), topicRequeste.getNome(), topicRequeste.getDescription(), topicRequeste.getCategoria()));
			if(add) {
				logger.info("topic adicionado com sucesso: " + topicRequeste.getId());
			}
			else {
				throw new TopicException("Erro ao adicionado topic, já existe este livro");
			}
		}
		else {
			throw new TopicException("Topic informado null");
		}
		
	}
	public Topic upDateTopic(TopicRequest topicUpDate, String id) {
		Topic topic = upDateTopicService(topicUpDate, id);
		if(topic != null) {
			return topic;
		}
		return null;
	}
	public List<Topic> getCategory(String categoria) {
		return getByCategory(categoria);
	}
	public void delete(String id) {
		Boolean delete = deleteTopic(id);
		if(delete) {
			logger.info("topic deletado com sucesso: " + id);
		}else {
			throw new TopicException("Erro ao deletar topic, id: "+ id);
		}
	}
}
