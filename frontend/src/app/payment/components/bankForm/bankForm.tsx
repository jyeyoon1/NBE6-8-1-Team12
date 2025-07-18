"use client";

export default function BankForm({ setPaymentInfo }) {
  const handleChange = (e) => {
    const { name, value } = e.target;
    setPaymentInfo(prev => ({ ...prev, [name]: value }));
  }
  return (
    <div className="space-y-4">
      <div>
        <label className="block text-sm font-medium text-gray-700">예금주명</label>
        <input
          type="text"
          name="accountHolder"
          onChange={handleChange}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
        />
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">계좌번호</label>
        <input name="accountNumber" onChange={handleChange} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm" />
      </div>
    </div>
  );
}