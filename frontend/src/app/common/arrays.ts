export function range(start: number, end?: number, step: number = 1): number[] {
    const result: number[] = [];
    if (end === undefined) {
        end = start;
        start = 0;
    }

    for (let i = start; i < end; i += step) {
        result.push(i);
    }

    return result;
}

export function toggle<T>(item: T, arr: Array<T>): Array<T> {
    const index = arr.indexOf(item);
    if (index !== -1) {
        return arr.filter((_, i) => i !== index);
    }
    return [...arr, item];
}
