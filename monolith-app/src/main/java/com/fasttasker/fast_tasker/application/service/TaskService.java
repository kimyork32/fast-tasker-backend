package com.fasttasker.fast_tasker.application.service;

import com.fasttasker.fast_tasker.application.dto.task.*;
import com.fasttasker.fast_tasker.application.dto.tasker.ChatProfileResponse;
import com.fasttasker.fast_tasker.application.dto.tasker.MinimalProfileResponse;
import com.fasttasker.fast_tasker.application.exception.*;
import com.fasttasker.fast_tasker.application.mapper.TaskMapper;
import com.fasttasker.fast_tasker.application.mapper.TaskerMapper;
import com.fasttasker.fast_tasker.domain.task.*;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasttasker.fast_tasker.application.exception.DomainException;

/**
 *
 */
@Slf4j
@Service
public class TaskService {

    private final ITaskRepository taskRepository;
    private final ITaskerRepository taskerRepository;
    private final TaskMapper taskMapper;
    private final TaskerMapper taskerMapper;

    public TaskService(
            ITaskRepository taskRepository,
            ITaskerRepository taskerRepository,
            TaskMapper taskMapper, TaskerMapper taskerMapper
    ) {
        this.taskRepository = taskRepository;
        this.taskerRepository = taskerRepository;
        this.taskMapper = taskMapper;
        this.taskerMapper = taskerMapper;
    }

    /**
     *
     */
    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest, UUID posterId) {
        Task newTask = taskMapper.toTaskEntity(taskRequest, posterId);

        Task savedTask = taskRepository.save(newTask);

        return taskMapper.toResponse(savedTask);
    }

    /**
     * return all active (status) tasks
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> listActiveTasks() {
        return taskRepository.findByStatus(TaskStatus.ACTIVE)
                .stream() // convert to stream
                .map(taskMapper::toResponse) // It applies a function (toResponse) to each element of
                // the stream and returns a new stream with the results (TaskResponse)
                .toList();  // after processing the stream elements, it reconstructs
        // the result into a concrete collection (List)
    }

    /**
     * return all tasks created by a specific user (Tasker)
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> listTasksByPoster(UUID posterId) {
        return taskRepository.findByPosterId(posterId)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID taskId) {
        Task task = findTask(taskId);
        return taskMapper.toResponse(task);
    }

    /**
     * return task complete (TaskResponse and MinimalProfileResponse)
     */
    @Transactional(readOnly = true)
    public TaskCompleteResponse getTaskCompleteById(UUID taskId) {
        Task task = findTask(taskId);
        Tasker tasker = taskerRepository.findById(task.getPosterId());

        MinimalProfileResponse profileResponse = taskerMapper.toMinimalProfileResponse(tasker);

        return taskMapper.toTaskCompleteResponse(task, profileResponse);
    }

    @Transactional
    public QuestionProfileResponse createQuestion(QuestionRequest questionRequest, UUID taskId, UUID askedById) {
        // Find task for the question
        Task task = findTask(taskId);
        Question question = taskMapper.toQuestionEntity(questionRequest, askedById, task);

        // Use business method instead of direct list manipulation
        task.addQuestion(question);

        // Save the task
        Task taskSaved = taskRepository.save(task);

        // Find the newly added question from the saved task entity
        Question savedQuestion = taskSaved.getQuestions().stream()
                .filter(q -> q.getId().equals(question.getId()))
                .findFirst()
                .orElseThrow(() -> new QuestionNotFoundException("Question was not saved correctly"));

        // Create minimalProfile
        Tasker tasker = taskerRepository.findById(question.getAskedById());

        MinimalProfileResponse profileResponse = taskerMapper.toMinimalProfileResponse(tasker);
        QuestionResponse questionResponse = taskMapper.toQuestionResponse(savedQuestion);

        return taskMapper.toQuestionProfileResponse(questionResponse, profileResponse, null);
    }

    @Transactional(readOnly = true)
    public List<QuestionProfileResponse> listQuestionsByTask(UUID taskId) {
        // find task
        Task task = findTask(taskId);
        // get questions from the task
        List<Question> questions = task.getQuestions();
        if (questions.isEmpty()) {
            return new ArrayList<>();
        }

        // list of tasker ids
        List<UUID> taskerIds = questions.stream()
                .flatMap(question ->
                        Stream.concat(
                                Stream.of(question.getAskedById()),
                                question.getAnswers().stream().map(Answer::getResponderId)
                        )
                )
                .distinct()
                .toList();

        // a single consultation, O(1)
        // map <taskerId, tasker>
        Map<UUID, Tasker> taskersById = loadTaskersMap(taskerIds);

        // build list of the question profile response (contain question and minimalProfile)
        return questions.stream()
                .map(question -> {
                    Tasker tasker = taskersById.get(question.getAskedById()); // obtain tasker to the map with the ID
                    MinimalProfileResponse profileResponse = taskerMapper.toMinimalProfileResponse(tasker);
                    QuestionResponse questionResponse = taskMapper.toQuestionResponse(question);
                    List<AnswerProfileResponse> answersResponse = question.getAnswers().stream()
                            .map(answer -> {
                                Tasker answerer = taskersById.get(answer.getResponderId());
                                // answererProfileResponse
                                ChatProfileResponse apr = taskerMapper.toChatProfileResponse(answerer);
                                // answeredResponse
                                AnswerResponse ar = taskMapper.toAnswerResponse(answer);
                                return taskMapper.toAnswerProfileResponse(ar, apr);
                            })
                            .toList();
                    return taskMapper.toQuestionProfileResponse(questionResponse, profileResponse, answersResponse);
                })
                .toList();

    }
    /**
     *
     */
    public void editTask() {
        // TODO implement here
    }

    /**
     * @param taskId
     */
    public void publishTask(UUID taskId) {
        // TODO implement here
    }

    /**
     *
     * @param offerRequest request of the offer
     * @param taskId id of the task
     */
    @Transactional
    public OfferProfileResponse createOffer(OfferRequest offerRequest, UUID taskId, UUID taskerId) {

        // Find task
        Task task = findTask(taskId);

        // Create offer with all required fields using the mapper
        // The mapper now accepts taskerId and task parameters
        Offer offer = taskMapper.toOfferEntity(offerRequest, taskerId, task);

        // Use the business method to add the offer to the task
        // This maintains encapsulation and applies domain validations
        task.addOffer(offer);

        // Save the task (cascade will save the offer)
        Task savedTask = taskRepository.save(task);

        // Find the saved offer from the task
        Offer savedOffer = savedTask.getOffers().stream()
                .filter(o -> o.getId().equals(offer.getId()))
                .findFirst()
                .orElseThrow(() -> new DomainException("Offer was not saved correctly"));

        // Get tasker profile
        Tasker tasker = taskerRepository.findById(taskerId);

        MinimalProfileResponse profileResponse = taskerMapper.toMinimalProfileResponse(tasker);
        OfferResponse offerResponse = taskMapper.toOfferResponse(savedOffer);

        return taskMapper.toOfferProfileResponse(offerResponse, profileResponse);
    }

    /**
     * get the list of the offer by taskId
     * @param taskId task id
     * @return list of offer
     */
    @Transactional(readOnly = true)
    public List<OfferProfileResponse> listOffersByTask(UUID taskId) {
        Task task = findTask(taskId);

        // get offers from the task
        List<Offer> offers = task.getOffers();

        if (offers.isEmpty()) {
            return new ArrayList<>();
        }

        // list of tasker ids
        List<UUID> taskerIds = offers.stream()
                .map(Offer::getOffertedById)
                .distinct()
                .toList();

        // a single consultation, O(1)
        // map <taskerId, tasker>
        Map<UUID, Tasker> taskersById = loadTaskersMap(taskerIds);

        // build list of the offer profile response (contain offer and minimalProfile)
        return offers.stream()
                .map(offer -> {
                    Tasker tasker = taskersById.get(offer.getOffertedById());
                    MinimalProfileResponse profileResponse = taskerMapper.toMinimalProfileResponse(tasker);
                    OfferResponse offerResponse = taskMapper.toOfferResponse(offer);
                    return taskMapper.toOfferProfileResponse(offerResponse, profileResponse);
                })
                .toList();
    }

    /**
     * Answers a question on a task.
     * Now uses task.getQuestionById() for better encapsulation.
     */
    @Transactional
    public AnswerProfileResponse answerQuestion(
            AnswerRequest answerRequest,
            UUID taskId,
            UUID responderId
    ) {
        UUID questionId = UUID.fromString(answerRequest.questionId());

        // Find task
        Task task = findTask(taskId);

        // Use the new query method instead of stream
        Question question = task.getQuestionById(questionId);

        Answer answer = taskMapper.toAnswerEntity(answerRequest, responderId, question);

        // Use business method to add answer
        question.addAnswer(answer);

        // Save updated task
        Task taskSaved = taskRepository.save(task);

        // Use the new query method to get the saved question
        Question questionSaved = taskSaved.getQuestionById(questionId);

        Answer answerSaved = questionSaved.getAnswers().stream()
                .filter(a -> a.getId().equals(answer.getId()))
                .findFirst()
                .orElseThrow(() -> new AnswerNotFoundException("TaskService. Answer not found"));

        // Find tasker
        Tasker tasker = taskerRepository.findById(responderId);

        ChatProfileResponse profileResponse = taskerMapper.toChatProfileResponse(tasker);
        AnswerResponse answerResponse = taskMapper.toAnswerResponse(answerSaved);
        return taskMapper.toAnswerProfileResponse(answerResponse, profileResponse);
    }
    /**
     * @param taskId
     * @param offerId
     */
    public void acceptOffer(UUID taskId, UUID offerId) {
        // TODO implement here
    }

    /**
     * @param taskId
     * @param askerId
     * @param question
     */
    public void askQuestion(UUID taskId, UUID askerId, String question) {
        // TODO implement here
    }

    /**
     * @param taskId
     */
    public void completeTask(UUID taskId) {
        // TODO implement here
    }

    /**
     * @param taskId
     */
    public void cancelTask(UUID taskId) {
        // TODO implement here
    }

    /**
     * @param city
     */
    public void listByLocation(String city) {
        // TODO implement here
    }

    /**
     * @param status
     */
    public void listByStatus(TaskStatus status) {
        // TODO implement here
    }

    // private methods (HELPERS)
    private Task findTask(UUID taskId) {
        return taskRepository.findById(taskId);
    }

    private Map<UUID, Tasker> loadTaskersMap(List<UUID> ids) {
        return taskerRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Tasker::getId, Function.identity()));
    }
}