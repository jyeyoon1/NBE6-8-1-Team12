"use client";

export default function CardForm({ paymentInfo, setPaymentInfo, validationErrors }: { paymentInfo: any, setPaymentInfo: (info: any) => void, validationErrors: any }) {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setPaymentInfo(prev => ({...prev, [name]: value}));
  };

  const handleCardNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const rawValue = e.target.value.replace(/\D/g, '');
    const formattedValue = rawValue.match(/.{1,4}/g)?.join('-') || '';
    setPaymentInfo(prev => ({...prev, cardNumber: formattedValue.slice(0, 19)}));
  };

  const handleExpiryDataChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const rawValue = e.target.value.replace(/\D/g, '');
    const formattedValue = rawValue.match(/.{1,2}/g)?.join('/') || '';
    setPaymentInfo(prev => ({...prev, expiryDate: formattedValue.slice(0, 5)}));
  };

  const handleCvcChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const rawValue = e.target.value.replace(/\D/g, '');
    setPaymentInfo(prev => ({...prev, cvc: rawValue.slice(0, 3)}));
  };

  return (
    <div className="space-y-4">
      <div>
        <label className="block text-sm font-medium text-gray-700">카드번호 </label>
        <input
          type="text"
          name="cardNumber"
          value={paymentInfo.cardNumber || ''}
          onChange={handleCardNumberChange}
          placeholder="0000-0000-0000-0000"
          className={`mt-1 block w-full px-3 py-2 border rounded-md ${validationErrors.cardNumber ? 'border-red-500' : 'border-gray-300'}`}
        />
        {validationErrors.cardNumber && <p className="text-red-500 text-sm mt-1">{validationErrors.cardNumber}</p>}
      </div>
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">유효기간</label>
          <input type="text" name="expiryDate" value={paymentInfo.expiryDate || ''} onChange={handleExpiryDataChange} placeholder="MM/YY" className={`mt-1 block w-full px-3 py-2 border rounded-md ${validationErrors.expiryDate ? 'border-red-500' : 'border-gray-300'}`} />
          {validationErrors.cardNumber && <p className="text-red-500 text-sm mt-1">{validationErrors.cardNumber}</p>}
        </div>
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">CVC</label>
        <input type="text" name="cvc" value={paymentInfo.cvc || ''} onChange={handleCvcChange} placeholder="000" className={`mt-1 block w-full px-3 py-2 border rounded-md ${validationErrors.cvc ? 'border-red-500' : 'border-gray-300'}`} />
        {validationErrors.cvc && <p className="text-red-500 text-sm mt-1">{validationErrors.cvc}</p>}
      </div>
    </div>
  );
}