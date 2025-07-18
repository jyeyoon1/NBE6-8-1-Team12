"use client";

export default function BankForm({ setPaymentInfo, validationErrors }: { setPaymentInfo: (info: any) => void, validationErrors: any }) {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => setPaymentInfo(prev => ({ ...prev, [e.target.name]: e.target.value }));
  return (
    <div className="space-y-4">
      <div>
        <label className="block text-sm font-medium text-gray-700">예금주명</label>
        <input
          type="text"
          name="accountHolder"
          onChange={handleChange}
          className={`mt-1 block w-full px-3 py-2 border rounded-md ${validationErrors.accountHolder ? 'border-red-500':'border-gray-300'}`} />
          {validationErrors.accountHolder && <p className="text-red-500 text-sm mt-1">{validationErrors.accountHolder}</p>}
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">계좌번호</label>
        <input name="accountNumber" onChange={handleChange} className={`mt-1 block w-full px-3 py-2 border rounded-md ${validationErrors.accountNumber ? 'border-red-500' : 'border-gray-300'}`} />
        {validationErrors.accountNumber && <p className="text-red-500 text-sm mt-1">{validationErrors.accountNumber}</p>}
      </div>
    </div>
  );
}