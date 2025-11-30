// --- DTOs de Ubicación (Asumidos de tus DTOs) ---
// (Tus DTOs LocationRequest y LocationResponse no estaban definidos,
// así que he creado uno genérico basado en el uso)
export type LocationData = {
  latitude: number;
  longitude: number;
  address: string;
  zip: number;
};

// --- DTOs de Cuenta ---

export type AccountStatus = 'ACTIVE' | 'PENDING' | 'SUSPENDED'; // Asumiendo los valores de tu Enum

// Basado en AccountResponse
export type Account = {
  id: string; // UUID se maneja como string en JSON
  email: string;
  status: AccountStatus;
};

// Basado en LoginRequest
export type LoginRequest = {
  email: string;
  rawPassword: string;
};

// Basado en LoginResponse
// (Asumimos que el backend establece una cookie HttpOnly
// y este DTO es solo una confirmación)
export type LoginResponse = {
  token: string;
};

// Basado en RegisterAccountRequest
export type SignupRequest = {
  email: string;
  rawPassword: string;
};

export interface SignupResponse {
  id: string;
  email: string;
  token: string;
}

export type ProfileData = {
  firstName: string;
  lastName: string;
  photo?: string;
  about?: string;
  location: LocationData
};

// --- DTOS de Complete Profile ---
export type CompleteProfileRequest = {
  profile: ProfileData;
};

export type CompleteProfileResponse = {
  id: string;
  accountId: string;
  profile: ProfileData;
  token: string;
};

// --- DTOs de Tarea ---

// Basado en TaskRequest
export type TaskRequest = {
  title: string;
  description: string;
  budget: number;
  location: LocationData;
  taskDate: string;
};

export type TaskResponse = {
  id: string;
  title: string;
  description: string;
  budget: number;
  location: LocationData;
  taskDate: string;
  status: string;
  posterId: string;
  poster: {
    id: string;
    firstName: string;
    lastName: string;
    photo?: string;
  };
  offerCount: number;
};

// --- DTOs de Chat ---
export interface MessageContent {
  text?: string;
  attachmentUrl?: string;
}

export interface Message {
  id: string;
  senderId: string;
  content: MessageContent;
  sentAt: string; // ISO String (Instant)
  isRead: boolean;
}

export interface ConversationSummary {
  conversationId: string;
  taskId: string;
  otherParticipantId: string;
  lastMessageSnippet: string;
}

// Payload para enviar al socket
export interface SendMessageRequest {
  conversationId: string;
  content: {
    text: string;
    attachmentUrl: string | null;
  };
}