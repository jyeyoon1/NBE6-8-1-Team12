
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