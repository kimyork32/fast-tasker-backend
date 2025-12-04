import { apiClient } from '@/lib/apiClient';
import { 
  TaskResponse, 
  TaskCompleteResponse,
  TaskRequest,
  OfferRequest,
  OfferProfileResponse,
  QuestionRequest,
  QuestionProfileResponse
} from '@/lib/types';

// Asumiendo que todos tus endpoints de tareas están en /tasks
const TASK_PREFIX = '/api/v1/tasks';

/**
 * Obtiene todas las tareas de los taskers.
 */
export const getPublicTasks = (): Promise<TaskResponse[]> => {
  return apiClient<TaskResponse[]>(TASK_PREFIX, {
    method: 'GET',
  }, true); // withCredentials = true (asumiendo que es una ruta protegida)
};

export const getMyTasks = (): Promise<TaskResponse[]> => {
  return apiClient<TaskResponse[]>(`${TASK_PREFIX}/my-tasks`, {
    method: 'GET',
  }, true); // withCredentials = true (asumiendo que es una ruta protegida)
};

/**
 * Obtiene una tarea por su ID.
 */
export const getTaskById = (id: string): Promise<TaskCompleteResponse> => {
  return apiClient<TaskCompleteResponse>(`${TASK_PREFIX}/${id}`, {
    method: 'GET',
  }, true); // withCredentials = true
};

/**
 * Crea una nueva tarea.
 */
export const createTask = (data: TaskRequest): Promise<TaskResponse> => {
  return apiClient<TaskResponse>(TASK_PREFIX, {
    method: 'POST',
    body: JSON.stringify(data),
  }, true); // withCredentials = true
};

/**
 * actualizar tarea existente
 */
export const updateTask = (id: string, data: Partial<TaskRequest>): Promise<TaskResponse> => {
  return apiClient<TaskResponse>(`${TASK_PREFIX}/${id}`, {
    method: 'PUT',
    body: JSON.stringify(data),
  }, true); // withCredentials = true
};

/**
 * eliminar tarea existente
 */
export const deleteTask = (id: string): Promise<void> => {
  return apiClient<void>(`${TASK_PREFIX}/${id}`,{
    method: 'DELETE',
  }, true);  // withCredentials = true
};

/**
 * crear oferta
 */
export const createOffer = (taskId: string, data: OfferRequest) : Promise<OfferProfileResponse> => {
  return apiClient<OfferProfileResponse>(`${TASK_PREFIX}/${taskId}/offers`,{
    method: 'POST',
    body: JSON.stringify(data),
  }, true);
}

/** 
 * obteer ofertas por task
 */
export const getOffersByTask = (taskId: string) : Promise<OfferProfileResponse[]> => {
  return apiClient<OfferProfileResponse[]>(`${TASK_PREFIX}/${taskId}/offers`,{
    method: 'GET',
  }, true);
}

/**
 * crear question
 */
export const createQuestion = (taskId: string, data: QuestionRequest) : Promise<QuestionProfileResponse> => {
  return apiClient<QuestionProfileResponse>(`${TASK_PREFIX}/${taskId}/questions`, {
    method: 'POST',
    body: JSON.stringify(data),
  }, true);
}

/**
 * obtener lista de preguntas por taskId
 */
export const getQuestionsByTask = (taskId: string) : Promise<QuestionProfileResponse[]> => {
  return apiClient<QuestionProfileResponse[]>(`${TASK_PREFIX}/${taskId}/questions`, {
    method: 'GET',
  }, true);
}