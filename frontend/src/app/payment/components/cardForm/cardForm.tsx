"use client";

export default function CardForm({ setPaymentInfo, validationErrors }: { setPaymentInfo: (info: any) => void, validationErrors: any }) {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => setPaymentInfo(prev => ({...prev, [e.target.name]: e.target.value}));
  return (
    <div className="space-y-4">
      <div>
        <label className="block text-sm font-medium text-gray-700">카드번호 </label>
        <input
          type="text"
          name="cardNumber"
          onChange={handleChange}
          placeholder="0000-0000-0000-0000"
          className={`mt-1 block w-full px-3 py-2 border rounded-md ${validationErrors.cardNumber ? 'border-red-500' : 'border-gray-300'}`}
        />
        {validationErrors.cardNumber && <p className="text-red-500 text-sm mt-1">{validationErrors.cardNumber}</p>}
      </div>
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">유효기간</label>
          <input type="text" name="expiryDate" onChange={handleChange} placeholder="MM/YY" className={`mt-1 block w-full px-3 py-2 border rounded-md ${validationErrors.expiryDate ? 'border-red-500' : 'border-gray-300'}`} />
          {validationErrors.cardNumber && <p className="text-red-500 text-sm mt-1">{validationErrors.cardNumber}</p>}
        </div>
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">CVC</label>
        <input type="text" name="cvc" onChange={handleChange} placeholder="000" className={`mt-1 block w-full px-3 py-2 border rounded-md ${validationErrors.cvc ? 'border-red-500' : 'border-gray-300'}`} />
        {validationErrors.cvc && <p className="text-red-500 text-sm mt-1">{validationErrors.cvc}</p>}
      </div>
    </div>
  );
}