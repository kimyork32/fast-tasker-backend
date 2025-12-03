"use client";

import React, { useState, useEffect, useRef } from 'react';
import { ConversationSummary, Message, SendMessageRequest } from '@/lib/types';
import { 
  getInbox, 
  getMessages, 
  connect, 
  disconnect, 
  sendMessage as sendChatMessage 
} from '@/services/chat.service';

export default function ChatPage() {
  // Estado de la UI
  const [inbox, setInbox] = useState<ConversationSummary[]>([]);
  const [activeChatId, setActiveChatId] = useState<string | null>(null);
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputText, setInputText] = useState("");
  const [currentUserId, setCurrentUserId] = useState<string | null>(null); // Para saber qué mensajes son 'míos'
  const [isConnected, setIsConnected] = useState(false);

  const messagesEndRef = useRef<HTMLDivElement>(null);

  // 1. Cargar Inbox al iniciar
  useEffect(() => {
    fetchInbox();
  }, []);

  // 2. Manejar conexión al seleccionar un chat
  useEffect(() => {
    if (activeChatId) {
      // a. Cargar historial antiguo (REST)
      fetchMessages(activeChatId);
      
      // b. Conectar al Socket (Real-time)
      connect(
        activeChatId, 
        handleNewMessage,
        setIsConnected
      );
    }

    // Cleanup: Desconectar si cambiamos de chat o salimos
    return () => {
      disconnect();
      setIsConnected(false);
    };
  }, [activeChatId]);

  // Scroll automático al último mensaje
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // --- MANEJADORES DE LÓGICA ---

  const fetchInbox = async () => {
    try {
      const inboxData = await getInbox();
      setInbox(inboxData);
    } catch (error) {
      console.error("Error cargando inbox:", error);
    }
  };

  const fetchMessages = async (conversationId: string) => {
    try {
      const history = await getMessages(conversationId);
      setMessages(history);
      // Inferir el ID del usuario actual si aún no lo tenemos
      if (!currentUserId && history.length > 0) {
        // Asumimos que el primer mensaje es del otro usuario, para identificar el nuestro
        // En una app real, esto vendría del perfil del usuario logueado.
        setCurrentUserId(history[0].senderId === inbox.find(c => c.conversationId === conversationId)?.otherParticipantId 
          ? 'tu-id-de-usuario-logueado' // Reemplazar con el ID real del usuario
          : history[0].senderId);
      }
    } catch (error) {
      console.error("Error cargando historial:", error);
    }
  };

  const handleNewMessage = (newMessage: Message) => {
    if (!currentUserId) {
      setCurrentUserId(newMessage.senderId);
    }
    setMessages((prev) => [...prev, newMessage]);
  }

  const handleSendMessage = () => {
    if (!inputText.trim() || !activeChatId) return;

    const payload: SendMessageRequest = {
      conversationId: activeChatId,
      content: {
        text: inputText,
        attachmentUrl: null // null si no hay foto
      }
    };
    
    sendChatMessage(payload);
    setInputText("");
  };

  // --- RENDERIZADO ---

  return (
    <div className="flex h-full bg-gray-100 -m-6"> {/* Ocupa el 100% del padre y anula el p-6 del layout */}
      
      {/* SIDEBAR (INBOX) */}
      <div className="w-1/3 bg-white border-r border-gray-300 overflow-y-auto">
        <div className="p-4 bg-gray-200 font-bold">Mis Conversaciones</div>
        <ul>
          {inbox.map((chat) => (
            <li 
              key={chat.conversationId}
              onClick={() => setActiveChatId(chat.conversationId)}
              className={`p-4 border-b cursor-pointer hover:bg-gray-50 ${activeChatId === chat.conversationId ? 'bg-blue-50' : ''}`}
            >
              <div className="font-semibold text-gray-800">Usuario: {chat.otherParticipantId.substring(0, 8)}...</div>
              <div className="text-sm text-gray-500 truncate">{chat.lastMessageSnippet}</div>
            </li>
          ))}
        </ul>
        {inbox.length > 0 ? (
          <ul>
            {inbox.map((chat) => (
              <li 
                key={chat.conversationId}
                onClick={() => setActiveChatId(chat.conversationId)}
                className={`p-4 border-b cursor-pointer hover:bg-gray-50 ${activeChatId === chat.conversationId ? 'bg-blue-50' : ''}`}
              >
                <div className="font-semibold text-gray-800">Usuario: {chat.otherParticipantId.substring(0, 8)}...</div>
                <div className="text-sm text-gray-500 truncate">{chat.lastMessageSnippet}</div>
              </li>
            ))}
          </ul>
        ) : (
          <div className="p-4 text-center text-gray-500">
            No tienes chats disponibles
          </div>
        )}
      </div>

      {/* CHAT AREA */}
      <div className="w-2/3 flex flex-col">
        {activeChatId ? (
          <>
            {/* HEADER */}
            <div className="p-4 bg-white border-b flex justify-between items-center shadow-sm">
              <h2 className="font-bold text-lg">Chat ID: {activeChatId.substring(0, 8)}...</h2>
              <div className={`w-3 h-3 rounded-full ${isConnected ? 'bg-green-500' : 'bg-red-500'}`} title={isConnected ? "Conectado" : "Desconectado"}></div>
            </div>

            {/* MENSAJES */}
            <div className="flex-1 overflow-y-auto p-4 space-y-4">
              {messages.map((msg) => {
                const isMe = msg.senderId === currentUserId;
                return (
                  <div key={msg.id} className={`flex ${isMe ? 'justify-end' : 'justify-start'}`}>
                    <div className={`max-w-xs md:max-w-md p-3 rounded-lg shadow ${isMe ? 'bg-blue-600 text-white rounded-br-none' : 'bg-white text-gray-800 rounded-bl-none border'}`}>
                      {/* Renderizar Foto si existe */}
                      {msg.content.attachmentUrl && (
                        <img src={msg.content.attachmentUrl} alt="Adjunto" className="mb-2 rounded-lg max-h-48 object-cover"/>
                      )}
                      
                      {/* Renderizar Texto */}
                      {msg.content.text && <p>{msg.content.text}</p>}
                      
                      <span className={`text-xs block text-right mt-1 ${isMe ? 'text-blue-200' : 'text-gray-400'}`}>
                        {new Date(msg.sentAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                      </span>
                    </div>
                  </div>
                );
              })}
              <div ref={messagesEndRef} />
            </div>

            {/* INPUT AREA */}
            <div className="p-4 bg-white border-t">
              <div className="flex gap-2">
                <input
                  type="text"
                  value={inputText}
                  onChange={(e) => setInputText(e.target.value)}
                  onKeyDown={(e) => e.key === 'Enter' && handleSendMessage()}
                  placeholder="Escribe un mensaje..."
                  className="flex-1 border rounded-lg p-2 focus:outline-none focus:ring-2 focus:ring-blue-500 text-black"
                />
                <button 
                  onClick={handleSendMessage}
                  disabled={!isConnected}
                  className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 disabled:bg-gray-400 transition"
                >
                  Enviar
                </button>
              </div>
            </div>
          </>
        ) : (
          <div className="flex-1 flex items-center justify-center text-gray-500">
            Selecciona una conversación para empezar
          </div>
        )}
      </div>
    </div>
  );
}