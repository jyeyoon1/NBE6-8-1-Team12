export type PaymentData = {
  id: number;
  amount: number;
  optionType: string;
  optionName: string;
};
export type PaymentItem = {
  id: number;
  purchaseId: number;
  paymentOptionType: string;
  paymentOptionName: string;
  paymentInfo: string | null;
  amount: number;
  paymentStatus: "PENDING" | "SUCCESS" | "FAILED" | "CANCELLED";
  date: string;
};
export type PaymentItemDetail = {
  id: number;
  purchaseId: number;
  purchaseEmail: string;
  paymentOptionType: string;
  paymentOptionName: string;
  paymentInfo: string | null;
  amount: number;
  paymentStatus: "PENDING" | "SUCCESS" | "FAILED" | "CANCELLED";
  date: string;
};
export type PaymentOption = {
  id: number;
  name: string;
};
export type PaymentOptionGroup = {
  optionType: string;
  options: PaymentOption[];
};
export type PaymentUpdateRequest = {
  paymentOptionId: number;
  paymentInfo: string;
  amount: number;
};