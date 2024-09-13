export type OptionsBase = {
    _type: 'base',
}

export type LoadingOptions = {
    _type: 'loading-options'
}

export type OptionsReady<T> = {
    _type: 'options-ready',
    items: Array<T>
}

export type LoadOptionsError = {
    _type: 'error'
}

export type OptionsStatus<T> = OptionsBase | LoadingOptions | OptionsReady<T> | LoadOptionsError
