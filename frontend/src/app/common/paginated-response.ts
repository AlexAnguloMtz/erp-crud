export type PaginatedResponse<T> = {
    pageNumber: number
    pageSize: number
    totalPages: number
    totalItems: number
    isLastPage: boolean
    items: Array<T>
}
