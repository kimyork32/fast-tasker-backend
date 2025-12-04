package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.task.*;
import com.fasttasker.fast_tasker.application.dto.tasker.MinimalProfileResponse;
import com.fasttasker.fast_tasker.application.exception.*;
import com.fasttasker.fast_tasker.application.mapper.TaskMapper;
import com.fasttasker.fast_tasker.application.mapper.TaskerMapper;
import com.fasttasker.fast_tasker.domain.account.Account;
import com.fasttasker.fast_tasker.domain.account.IAccountRepository;
import com.fasttasker.fast_tasker.domain.notification.INotificationRepository;
import com.fasttasker.fast_tasker.domain.task.*;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class TaskService {

    private final ITaskRepository taskRepository;
    private final ITaskerRepository taskerRepository;
    private final INotificationRepository notificationRepository;
    private final IAccountRepository accountRepository;
    private final TaskMapper taskMapper;
    private final TaskerMapper taskerMapper;

    public TaskService(
            ITaskRepository taskRepository,
            ITaskerRepository taskerRepository,
            INotificationRepository notificationRepository, IAccountRepository accountRepository, TaskMapper taskMapper, TaskerMapper taskerMapper
    ) {
        this.taskRepository = taskRepository;
        this.taskerRepository = taskerRepository;
        this.notificationRepository = notificationRepository;
        this.accountRepository = accountRepository;
        this.taskMapper = taskMapper;
        this.taskerMapper = taskerMapper;
    }

    /**
     *
     */
    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest, UUID posterId) {
        Task newTask = taskMapper.toTaskEntity(taskRequest);

        // assign the values here, ignoring what comes from the client
        newTask.setPosterId(posterId);
        newTask.setStatus(TaskStatus.ACTIVE); // assign the default initial state

        taskRepository.save(newTask);

        return taskMapper.toResponse(newTask);
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
                .collect(Collectors.toList());  // after processing the stream elements, it reconstructs
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
                .collect(Collectors.toList());
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
        Tasker tasker = taskerRepository.findById(task.getPosterId())
                .orElseThrow(() -> new TaskerNotFoundException("TaskService. getTaskById exception: invalid tasker ID"));

        MinimalProfileResponse profileResponse = taskerMapper.toMinimalProfileResponse(tasker);

        return taskMapper.toTaskCompleteResponse(task, profileResponse);
    }

    @Transactional
    public QuestionProfileResponse createQuestion(QuestionRequest questionRequest, UUID taskId, UUID accountId) {
        // find task for the insert question
        Task task = findTask(taskId);
        Question question = taskMapper.toQuestionEntity(questionRequest);
        // insert values
        question.setStatus(QuestionStatus.PENDING);
        question.setAskedById(accountId);
        question.setCreatedAt(Instant.now());
        question.setTask(task);

        // insert question in the task
        task.getQuestions().add(question);

        // and save
        Task taskSaved = taskRepository.save(task);

        // check if the questions saved is equal to the questions will be saved
        // Find the newly added offer from the saved task entity to ensure we return the persisted state.
        Question savedQuestions = taskSaved.getQuestions().stream()
                .filter(q -> q.getId().equals(question.getId())).findFirst().orElse(question);

        // create minimalProfile
        Tasker tasker = taskerRepository.findById(question.getAskedById())
                .orElseThrow(() -> new TaskerNotFoundException("TaskService. createQuestion. task not found"));

        MinimalProfileResponse profileResponse = taskerMapper.toMinimalProfileResponse(tasker);
        // create profileResponse
        QuestionResponse questionResponse = taskMapper.toQuestionResponse(savedQuestions);

        return taskMapper.toQuestionProfileResponse(questionResponse, profileResponse);
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
                .map(Question::getAskedById)
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
                    return taskMapper.toQuestionProfileResponse(questionResponse, profileResponse);
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
     * @param accountId id of the tasker, find the taskId with this
     */
    @Transactional
    public OfferProfileResponse createOffer(OfferRequest offerRequest, UUID taskId, UUID accountId) {
        // find taskerId with the accountId
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("TaskerService. Account not found"));
        UUID taskerId = account.getTaskerId();

        // find task
        Task task = findTask(taskId);

        Offer offer = taskMapper.toOfferEntity(offerRequest);
        // insert values of the offer
        offer.setStatus(OfferStatus.PENDING);
        offer.setOffertedById(taskerId); // Corrected: Should be the ID of the user making the offer
        offer.setCreatedAt(Instant.now());
        offer.setTask(task);

        // add the offer to the task
        task.getOffers().add(offer);

        Task savedTask = taskRepository.save(task);

        // Find the newly added offer from the saved task entity to ensure we return the persisted state.
        Offer savedOffer = savedTask.getOffers().stream()
                .filter(o -> o.getId().equals(offer.getId())).findFirst().orElse(offer);

        Tasker tasker = taskerRepository.findById(taskerId)
                .orElseThrow(() -> new TaskNotFoundException("TaskerService. Tasker not found"));

        MinimalProfileResponse  profileResponse = taskerMapper.toMinimalProfileResponse(tasker);
        OfferResponse offerResponse = taskMapper.toOfferResponse(offer);

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

    public AnswerProfileResponse answerQuestion(
            AnswerRequest answerRequest,
            UUID taskId,
            UUID accountId
    ) {
        // find taskerId with the accountId
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("TaskerService. Account not found"));

        UUID taskerId = account.getTaskerId();
        UUID questionId = UUID.fromString(answerRequest.questionId());

        // find task
        Task task = findTask(taskId);

        // find question
        Question question = task.getQuestions().stream()
                .filter(q -> q.getId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new QuestionNotFoundException("TaskerService. Question not found"));

        Answer answer = taskMapper.toAnswerEntity(answerRequest);

        // insert values of the answer
        answer.setQuestionId(questionId);
        answer.setAnsweredId(taskerId);
        answer.setCreatedAt(Instant.now());
        answer.setQuestion(question);



        // add the answer to the question
        question.getAnswers().add(answer);

        // save updated task
        Task taskSaved = taskRepository.save(task);

        Question questionSaved = taskSaved.getQuestions().stream()
                .filter(q -> q.getId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new QuestionNotFoundException("TaskerService. Question not found"));

        Answer answerSaved = questionSaved.getAnswers().stream()
                .filter(a -> a.getId().equals(answer.getId()))
                .findFirst()
                .orElseThrow(() -> new AnswerNotFoundException("TaskerService. Answer not found"));

        // find tasker
        Tasker tasker = taskerRepository.findById(taskerId)
                .orElseThrow(() -> new TaskNotFoundException("TaskerService. Tasker not found"));

        MinimalProfileResponse  profileResponse = taskerMapper.toMinimalProfileResponse(tasker);
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

    public TaskMapper getTaskMapper() {
        return taskMapper;
    }

    // private methods (HELPERS)
    private Task findTask(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("TaskService. Task not found"));
    }

    private Map<UUID, Tasker> loadTaskersMap(List<UUID> ids) {
        return taskerRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Tasker::getId, Function.identity()));
    }
}