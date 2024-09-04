export enum DayPart {
    DAY = 'day',
    AFTERNOON = 'afternoon',
    NIGHT = 'night'
}

export function getDayPart(date: Date): DayPart {
    const hour = date.getHours();
    if (hour >= 4 && hour < 12) {
        return DayPart.DAY;
    }

    if (hour >= 12 && hour < 19) {
        return DayPart.AFTERNOON;
    }

    return DayPart.NIGHT;

}

export function getSalutation(dayPart: DayPart): string {
    if (dayPart === DayPart.DAY) {
        return 'Buenos días';
    }

    if (dayPart === DayPart.AFTERNOON) {
        return 'Buenas tardes';
    }

    return 'Buenas noches';
}