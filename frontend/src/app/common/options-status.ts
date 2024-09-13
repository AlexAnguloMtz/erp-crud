type OptionsBase = {
    _type: 'base',
}

type LoadingOptions = {
    _type: 'loading-options'
}

type OptionsReady<T> = {
    _type: 'options-ready',
    items: Array<T>
}

type LoadOptionsError = {
    _type: 'error'
}

export type OptionsStatus<T> = OptionsBase | LoadingOptions | OptionsReady<T> | LoadOptionsError

export function loadingOptions<T>(optionsStatus: OptionsStatus<T>): boolean {
    return optionsStatus._type === 'loading-options';
}

export function loadingError<T>(optionsStatus: OptionsStatus<T>): boolean {
    return optionsStatus._type === 'error';
}


export function options<T>(optionsStatus: OptionsStatus<T>): Array<T> {
    if (optionsStatus._type !== 'options-ready') {
        return [];
    }
    return optionsStatus.items;
}