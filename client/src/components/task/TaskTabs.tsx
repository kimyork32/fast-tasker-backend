'use client';

import { OffersList } from './OffersList';
import { OfferProfileResponse } from '@/lib/types';
import { QuestionsList } from './QuestionsList';

interface Question {
  id: string;
  author: string;
  avatarColor?: string;
  message: string;
  timestamp: string;
  answer?: { author: string; message: string; timestamp: string };
}

interface TaskTabsProps {
  offers: OfferProfileResponse[];
  isLoadingOffers: boolean;
  questions: Question[];
  numOffers: number;
  numQuestions: number;
  activeTab: 'offers' | 'questions';
  onTabChange: (tab: 'offers' | 'questions') => void;
}

export function TaskTabs({
  offers,
  isLoadingOffers,
  questions,
  numOffers,
  numQuestions,
  activeTab,
  onTabChange,
}: TaskTabsProps) {
  return (
    <div className="md:col-span-12 bg-white rounded-3xl p-2 shadow-sm border border-gray-200/60">
      <div className="flex bg-gray-100 p-1 rounded-2xl w-full md:w-fit mb-6">
        <button
          onClick={() => onTabChange('offers')}
          className={`flex-1 md:flex-none px-6 py-3 rounded-xl text-sm font-bold transition-all ${
            activeTab === 'offers' ? 'bg-white text-black shadow-sm' : 'text-gray-500 hover:text-gray-700'
          }`}
        >
          Ofertas ({offers.length > 0 ? offers.length : numOffers})
        </button>
        <button
          onClick={() => onTabChange('questions')}
          className={`flex-1 md:flex-none px-6 py-3 rounded-xl text-sm font-bold transition-all ${
            activeTab === 'questions' ? 'bg-white text-black shadow-sm' : 'text-gray-500 hover:text-gray-700'
          }`}
        >
          Preguntas ({numQuestions})
        </button>
      </div>

      <div className="px-6 pb-8 min-h-[200px]">
        {activeTab === 'offers' ? (
          <OffersList offers={offers} isLoading={isLoadingOffers} />
        ) : (
          <QuestionsList questions={questions} />
        )}
      </div>
    </div>
  );
}
