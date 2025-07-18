export type PageResponse<T> = {
    content: T[];
    pageNumber: number;
    pageSize: number;
    totalPages: number;
    totalElements: number;
    isLast: boolean;
};