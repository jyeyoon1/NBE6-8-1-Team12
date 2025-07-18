"use client";

export default function CardForm({ setPaymentInfo }) {
  const handleChange = (e) => {
    const { name, value } = e.target;
    setPaymentInfo(prev => ({ ...prev, [name]: value }));
  }
  return (
    <div className="space-y-4">
      <div>
        <label className="block text-sm font-medium text-gray-700">카드번호 </label>
        <input
          type="text"
          name="cardNumber"
          onChange={handleChange}
          placeholder="0000-0000-0000-0000"
          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md"
        />
      </div>
      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">유효기간</label>
          <input type="text" name="expiryDate" onChange={handleChange} placeholder="MM/YY" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md" />
        </div>
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">CVC</label>
        <input type="text" name="cvc" onChange={handleChange} placeholder="000" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md" />
      </div>
    </div>
  );
}