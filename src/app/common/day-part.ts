export enum DayPart {
    DAY = 'day',
    NIGHT = 'night'
}

export function getDayPart(date: Date): DayPart {
    const hour = date.getHours();
    if (hour >= 6 && hour < 18) {
        return DayPart.DAY;
    } else {
        return DayPart.NIGHT;
    }
}