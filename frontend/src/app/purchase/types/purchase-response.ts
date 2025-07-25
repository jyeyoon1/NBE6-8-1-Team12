// 주문 페이지 연관
export interface PurchaseItemInfo {
  productId: number;
  productName: string;
  price: number;
  imageUrl: string;
  quantity: number;
  totalPrice: number;
}

export type PaymentOptionType = "TOP_LEVEL" | "DETAIL";

export interface PaymentOption {
  id: number;
  parentId: number | null;
  name: string;
  type: PaymentOptionType;
  code: string;
  sortSeq: number;
}

export interface PurchasePageResBody {
  purchaseItems: PurchaseItemInfo[];
  paymentOptions: PaymentOption[];
}

export interface PurchaseCheckoutResBody {
  purchaseId: number;
  paymentOptionId: number;
  amount: number;
}


// 주문 조회 Form 연관
export interface PurchaseLookupResBody {
  userEmail: string;
  purchaseId: number;
}


// 주문 상세 페이지 연관
export interface PurchaseDto {
  purchaseId: number;
  userEmail: string;
  totalPrice: number;
  purchaseStatus: PurchaseStatus;
  purchaseDate: string;
}

export type PurchaseStatus = "TEMPORARY" | "PURCHASED" | "CANCELED" | "FAILED";

export interface PurchaseItemDetailDto {
  purchaseItemId: number;
  quantity: number;
  price: number;
  productName: string;
  imageUrl: string;
}

export interface ReceiverResDto {
  name: string;
  phoneNumber: string;
  postcode: number;
  address: string;
  status: ShippingStatus;
  modifyDate: string;
}

export type ShippingStatus = "TEMPORARY" | "BEFORE_DELIVERY" | "DELIVERING" | "DELIVERED";

export interface PurchaseDetailDto {
  purchase: PurchaseDto;
  purchaseItems: Array<PurchaseItemDetailDto>;
  receiver: ReceiverResDto;
}


// 관리자 주문 목록 페이지 연관
export interface PurchaseAdmDto {
  purchaseId: number;
  userEmail: string;
  totalPrice: number;
  totalQuantity: number;
  purchaseStatus: PurchaseStatus;
  purchaseDate: string;
  summaryName: string;
}

export interface PageResponseDto<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
  isLast: boolean;
}

export interface ServerResponse<T> {
  resultCode: string;
  statusCode: number;
  msg: string;
  data: T;
}