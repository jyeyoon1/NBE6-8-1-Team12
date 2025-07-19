// 주문 페이지 연관
export interface PurchaseInfo {
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
    purchaseInfo: PurchaseInfo;
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

export type PurchaseStatus = "TEMPORARY" | "ORDERED" | "CANCELED" | "FAILED";

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