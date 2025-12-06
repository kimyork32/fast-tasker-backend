// src/app/(dashboard)/tasks/[taskId]/page.tsx
"use client";

import { useState, useEffect, use } from 'react';
import { 
  getTaskById, 
  createOffer, 
  getOffersByTask,
  createQuestion,
  getQuestionsByTask
} from '@/services/task.service';
import { 
  OfferRequest, 
  OfferProfileResponse, 
  TaskCompleteResponse,
  QuestionRequest,
  QuestionProfileResponse
} from '@/lib/types';
import Cookies from 'js-cookie'; // Importa para acceder a las cookies en el cliente
import { jwtDecode } from 'jwt-decode'; // Importa para decodificar el token JWT
import Link from 'next/link';
import { TaskHeader } from '@/components/task/TaskHeader';
import { TaskPricePanel } from '@/components/task/TaskPricePanel';
import { TaskQuickInfo } from '@/components/task/TaskQuickInfo';
import { TaskDescription } from '@/components/task/TaskDescription';
import { OfferModal } from '@/components/task/OfferModal'
import { TaskTabs } from '@/components/task/TaskTabs'

type TaskDetailPageProps = {
  params: Promise<{ taskId: string }>;
};

export default function TaskDetailPage({ params }: TaskDetailPageProps) {
  const { taskId } = use(params); // taskId sigue siendo el ID en bruto de la URL

  const [taskComplete, setTask] = useState<TaskCompleteResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [activeTab, setActiveTab] = useState<'offers' | 'questions'>('questions');
  
  // Estado para almacenar el ID del usuario actual
  const [currentUserId, setCurrentUserId] = useState<string | null>(null);

  // Nuevo Estado para el Modal de Oferta
  const [isOfferModalOpen, setIsOfferModalOpen] = useState(false);

  // estados para las ofertas
  const [offers, setOffers] = useState<OfferProfileResponse[]>([]);
  const [isLoadingOffers, setIsLoadingOffers] = useState(false);

  // Estados para el formulario de la oferta
  const [offerPrice, setOfferPrice] = useState<number>(0);
  const [offerMessage, setOfferMessage] = useState('');

  // estado para el question
  const [questions, setQuestions] = useState<QuestionProfileResponse[]>([]);
  const [questionDescription, setQuestionDescription] = useState('');
  const [isLoadingQuestions, setIsLoadingQuestions] = useState(false);

  // Efecto para obtener el ID del usuario actual de la cookie
  useEffect(() => {
    const token = Cookies.get('jwtToken');
    if (token) {
      try {
        // Decodifica el token para obtener el payload.
        // 'sub' (subject) usualmente contiene el ID del usuario.
        const decodedToken: { sub: string } = jwtDecode(token);
        const userIdFromToken = decodedToken.sub;
        setCurrentUserId(userIdFromToken);
        console.log(`ID de usuario obtenido del token: ${userIdFromToken}`); // Logueamos el valor adirecto
      } catch (error) {
        console.error("Error al decodificar el token JWT:", error);
      }
    }
  }, []);

  useEffect(() => {
    getTaskById(taskId)
      .then(data => {
        setTask(data);
        console.log(`ID de usuario del tasker: ${data?.task?.posterId}`);
      })
      .catch(console.error)
      .finally(() => setIsLoading(false));
  }, [taskId]);

  // Cargar ofertas cuando la pestaña de ofertas se activa
  useEffect(() => {
    if (activeTab === 'offers' && taskId) {
      setIsLoadingOffers(true);
      getOffersByTask(taskId)
        .then(setOffers)
        .catch(console.error)
        .finally(() => setIsLoadingOffers(false));
    }
  }, [activeTab, taskId]);

  // Cargar preguntas cuando la pestaña de preguntas se activa
  useEffect(() => {
    if (activeTab === 'questions' && taskId) {
      setIsLoadingQuestions(true);
      getQuestionsByTask(taskId)
        .then(setQuestions)
        .catch(console.error)
        .finally(() => setIsLoadingQuestions(false));
    }
  }, [activeTab, taskId])

  // Setea el precio inicial cuando el modal se abre y la tarea está cargada
  useEffect(() => {
    if (isOfferModalOpen && taskComplete?.task.budget) {
      setOfferPrice(taskComplete.task.budget);
    }
  }, [isOfferModalOpen, taskComplete]);

    // HANDLE OFFER
  const handleOfferSubmit = async () => {
    if (!taskComplete) return;

    const offerRequest: OfferRequest = {
      price: offerPrice,
      description: offerMessage,
    };

    try {
      const newOffer = await createOffer(taskComplete.task.id, offerRequest);
      setOffers(prevOffers => [newOffer, ...prevOffers]);
      setActiveTab('offers');
      setIsOfferModalOpen(false);
    } catch (error) {
      console.error("Error al crear la oferta:", error);
      alert("Hubo un error al enviar tu oferta. Inténtalo de nuevo.");
    }
  };

  // HANDLE QUESTION
  const handleQuestionSubmit = async () => {
    if (!taskComplete) return;

    const questionRequest : QuestionRequest = {
      description: questionDescription
    };
    
    try {
      const newQuestion = await createQuestion(taskComplete.task.id, questionRequest);
      setQuestions(prevQuestions => [newQuestion, ...prevQuestions]);
      setTask(prevTask => {
        if (!prevTask) return null;
        return {
          ...prevTask,
          numQuestions: prevTask.numQuestions + 1,
        };
      });
    } catch(error) {
      console.error("Error al crear la pregunta:", error);
      alert("Hubo un error al enviar tu pregunta. Inténtalo de nuevo.");
    }
  }

  if (isLoading) return <div className="min-h-screen flex items-center justify-center bg-gray-100"><div className="w-8 h-8 border-4 border-gray-300 border-t-black rounded-full animate-spin"></div></div>;
  if (!taskComplete) return <div className="min-h-screen flex items-center justify-center bg-gray-100 text-gray-500">No se encontró la tarea.</div>;

  // Determinar si el usuario actual es el creador de la tarea
  const isCreator = currentUserId && taskComplete.task?.posterId === currentUserId;

  return (
    <>
    <div className="min-h-screen bg-gray-100 p-4 md:p-8 font-sans">
      
      {/* Top Navigation */}
      <div className="max-w-7xl mx-auto mb-6 flex justify-between items-center">
        <Link href="/tasks" className="text-sm font-medium text-gray-500 hover:text-black transition flex items-center gap-1">
          ← Volver
        </Link>
        <span className="text-xs font-mono text-gray-400 uppercase tracking-widest">ID: {taskComplete.task?.id.substring(0,8)}...</span> {/* Usa taskComplete.task.id para consistencia */}
      </div>

      {/* GRID LAYOUT PRINCIPAL */}
      <main className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-12 gap-4 md:gap-6">

        {/* 1. BLOQUE TÍTULO */}
        <TaskHeader
          title={taskComplete.task?.title}
          status={taskComplete.task?.status}
          type={taskComplete.task?.type || "Limpieza"} // PONER ETIQUETA
          publishedAgo={taskComplete.task?.createAt} // PONER TIEMPO DE CREACION
          // views={taskComplete.task?.views} // PONER VISUALIZACIONES
          views={23}
        />

        {/* 2. BLOQUE PRECIO Y ACCIÓN (Activa el Modal) / BOTÓN MODIFICAR */}
        {isCreator ? (
          <Link href={`/tasks/${taskId}/edit`} className="md:col-span-4 bg-blue-500 text-white p-4 rounded-lg flex items-center justify-center hover:bg-blue-600 transition-colors">
            Modificar Tarea
          </Link>
        ) : (
          <TaskPricePanel
            budget={taskComplete.task?.budget || 0}
            onOpenOfferModal={() => setIsOfferModalOpen(true)}
          />
        )}

        {/* 3. BLOQUE INFO RÁPIDA */}
        <TaskQuickInfo
          date={taskComplete.task?.taskDate}
          location={taskComplete.task.location?.address}
        />

        {/* 4. BLOQUE DESCRIPCIÓN */}
        <TaskDescription
          description={taskComplete.task?.description}
          images={taskComplete.task?.images || []} // PONER IMAGEENES EL ATRIBUTO
        />
        
        {/* 5. BLOQUE TABS / ACTIVIDAD (Comentarios y Preguntas) */}
        <TaskTabs
          offers={offers}
          questions={questions}
          isLoadingOffers={isLoadingOffers}
          numOffers={taskComplete.numOffers}
          numQuestions={taskComplete.numQuestions}
          activeTab={activeTab}
          questionDescription={questionDescription}
          onTabChange={setActiveTab}
          onQuestionSubmit={handleQuestionSubmit}
          setQuestionDescription={setQuestionDescription}
        />


      </main>
    </div>

    {/* =================================================================
        MODAL FLOTANTE DE OFERTA
       ================================================================= */}
    {!isCreator && (
      <OfferModal
        isOpen={isOfferModalOpen}
        budget={taskComplete?.task.budget || 0}
        offerPrice={offerPrice}
        offerMessage={offerMessage}
        setOfferPrice={setOfferPrice}
        setOfferMessage={setOfferMessage}
        onClose={() => setIsOfferModalOpen(false)}
        onSubmit={handleOfferSubmit}
      />
    )}

    </>
  );
}