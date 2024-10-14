import { Injectable } from "@angular/core";
import { Authentication, AuthenticationHolder } from "./authentication-holder";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment";

const PUBLIC_PATHS: Array<string> = [
    '/api/v1/auth/login'
];

// Accepts Date objects
type RawParams = { [param: string]: string | number | boolean | Date | ReadonlyArray<string | number | boolean | Date> }

// Does not accept Date objects
type ParsedParams = { [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean> }

export type ApiClientOptions = {
    params?: RawParams,
    responseType?: 'json' | 'arraybuffer'
}

export class InvalidAuthenticationError extends Error {
    constructor(message: string) {
        super(message);
        this.name = 'InvalidAuthenticationError';
    }
}

@Injectable({
    providedIn: 'root'
})
export class ApiClient {

    constructor(
        private http: HttpClient,
        private authenticationHolder: AuthenticationHolder,
    ) { }

    get<T>(path: string, options?: ApiClientOptions): Observable<T>;

    get(path: string, options?: ApiClientOptions): Observable<ArrayBuffer>;

    get<T>(path: string, options?: ApiClientOptions): Observable<T | ArrayBuffer> {
        // Prepare url, headers and params
        const url = this.prepareUrl(path);
        const headers = this.makeHeaders(path);
        const params = this.parseParams(options?.params);

        // Return response as raw binary data (ArrayBuffer) only if necessary
        if (options && options.responseType === 'arraybuffer') {
            return this.http.get(url, {
                headers,
                params,
                responseType: 'arraybuffer'
            })
        }

        // Return response as json by default 
        return this.http.get<T>(url, {
            headers,
            params,
            responseType: 'json'
        });
    }

    post<T>(path: string, body: any | null, options?: ApiClientOptions): Observable<T> {
        return this.http.post<T>(this.prepareUrl(path), body, { headers: this.makeHeaders(path), params: this.parseParams(options?.params) });
    }

    put<T>(path: string, body: any | null, options?: ApiClientOptions): Observable<T> {
        return this.http.put<T>(this.prepareUrl(path), body, { headers: this.makeHeaders(path), params: this.parseParams(options?.params) });
    }

    delete<T>(path: string, options?: ApiClientOptions): Observable<T> {
        return this.http.delete<T>(this.prepareUrl(path), { headers: this.makeHeaders(path), params: this.parseParams(options?.params) });
    }

    private prepareUrl(path: string): string {
        return environment.apiUrl + path;
    }

    private makeHeaders(path: string): { [key: string]: string } {
        const headers: { [key: string]: string } = {};

        if (!this.isPublicPath(path)) {
            headers['Authorization'] = `Bearer ${this.getAccessToken()}`;
        }

        return headers;
    }

    private parseParams(params?: RawParams): ParsedParams {
        if (!params) {
            return {};
        }
        // Parse all nested Dates as ISO strings
        return JSON.parse(JSON.stringify(params));
    }

    private getAccessToken(): string {
        const authentication: Authentication | undefined = this.authenticationHolder.getAuthentication();

        if (!authentication || !this.authenticationHolder.isValidAuthentication(authentication)) {
            // TODO 
            // IMPORTANT: Catch this error SOMEWHERE and DO SOMETHING!!!
            throw new InvalidAuthenticationError("cannot use authentication because it is invalid");
        }

        return authentication.token;
    }

    private isPublicPath(path: string) {
        return PUBLIC_PATHS.includes(path);
    }


}