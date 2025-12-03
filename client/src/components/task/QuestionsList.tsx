'use client';

interface Question {
  id: string;
  author: string;
  avatarColor?: string;
  message: string;
  timestamp: string;
  answer?: { author: string; message: string; timestamp: string };
}

interface QuestionsListProps {
  questions: Question[];
}

export function QuestionsList({ questions }: QuestionsListProps) {
  return (
    <div className="max-w-3xl">
      {/* Caja para escribir pregunta */}
      <div className="flex gap-4 mb-10">
        <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-bold shrink-0">YO</div>
        <div className="flex-1">
          <textarea
            rows={3}
            placeholder="Haz una pregunta pública sobre esta tarea..."
            className="w-full border border-gray-200 rounded-xl p-4 text-sm focus:ring-2 focus:ring-black focus:border-transparent outline-none resize-none bg-gray-50"
          ></textarea>
          <div className="flex justify-end mt-2">
            <button className="px-6 py-2 bg-blue-600 text-white text-sm font-bold rounded-full hover:bg-blue-700 transition">
              Publicar pregunta
            </button>
          </div>
        </div>
      </div>

      {/* Lista de preguntas */}
      <div className="space-y-8">
        {questions.map(q => (
          <div key={q.id} className="flex gap-4 group">
            <div className={`w-10 h-10 rounded-full flex items-center justify-center text-xs font-bold shrink-0 ${q.avatarColor || 'bg-gray-200 text-gray-500'}`}>
              {q.author.slice(0, 2).toUpperCase()}
            </div>
            <div className="flex-1">
              <div className="flex items-center gap-2 mb-1">
                <span className="font-bold text-gray-900 text-sm">{q.author}</span>
                <span className="text-xs text-gray-400">{q.timestamp}</span>
              </div>
              <p className="text-gray-700 text-sm leading-relaxed">{q.message}</p>

              {q.answer && (
                <div className="mt-4 flex gap-3 pl-4 border-l-2 border-gray-100">
                  <div className="w-6 h-6 rounded-full bg-black text-white flex items-center justify-center text-[10px] font-bold shrink-0">UP</div>
                  <div>
                    <div className="flex items-center gap-2 mb-1">
                      <span className="font-bold text-black text-xs px-1.5 py-0.5 bg-gray-100 rounded">Poster</span>
                      <span className="text-xs text-gray-400">{q.answer.timestamp}</span>
                    </div>
                    <p className="text-gray-600 text-sm">{q.answer.message}</p>
                  </div>
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
