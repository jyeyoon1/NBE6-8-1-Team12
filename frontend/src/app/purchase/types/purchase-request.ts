export type PurchasePageReqBody = {
  purchaseItems: {
    productId: number;
    price: number;
    quantity: number;
    totalPrice: number;
  }[];
  purchaser: {
    name: string;
    email: string;
  };
  receiver: {
    name: string;
    phoneNumber: string;
    address: string;
    postcode: number;
    email: string;
  };
  paymentOptionId: number;
};

export type PurchaserReqBody = {
  userEmail: string;
  purchaseId: number;
};
