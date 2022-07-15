export interface Product {
  referenceId?: string;
  name?: string;
  description?: string;
  benefits?: string[];
  costsFrequency?: string;
  cost?: Cost;
  imageUrl?: string;
  detailedProductDescriptionUrl?: string;
  productType?: string;
  maxQuantity?: number;
  productDisclaimer?: string;
  priceDisclaimer?: string;
  [property: string]: any;
}

export interface Cost {
  value: number;
  currency: string;
}

export interface ProductPayload {
  productName?: string;
  referenceId?: string;
}
