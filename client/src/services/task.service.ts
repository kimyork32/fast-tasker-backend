import { apiClient } from '@/lib/apiClient';
import { Task, TaskRequest } from '@/lib/types';

// Asumiendo que todos tus endpoints de tareas están en /tasks
const TASK_PREFIX = 'api/v1/tasks';

/**
 * Obtiene todas las tareas.
 */
export const getTasks = (): Promise<Task[]> => {
  return apiClient<Task[]>(TASK_PREFIX, {
    method: 'GET',
  }, true); // withCredentials = true (asumiendo que es una ruta protegida)
};

/**
 * Obtiene una tarea por su ID.
 */
export const getTaskById = (id: string): Promise<Task> => {
  return apiClient<Task>(`${TASK_PREFIX}/${id}`, {
    method: 'GET',
  }, true); // withCredentials = true
};

/**
 * Crea una nueva tarea.
 */
export const createTask = (data: TaskRequest): Promise<Task> => {
  return apiClient<Task>(TASK_PREFIX, {
    method: 'POST',
    body: JSON.stringify(data),
  }, true); // withCredentials = true
};