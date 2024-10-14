import { Injectable } from "@angular/core";

const TOKEN_KEY: string = 'auth-token';

export type Authentication = {
    token: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthenticationHolder {

    getAuthentication(): Authentication | undefined {
        const token: string | null = localStorage.getItem(TOKEN_KEY);

        if (!token) {
            return undefined;
        }

        return {
            token,
        };
    }

    setAuthentication(authentication: Authentication) {
        localStorage.setItem(TOKEN_KEY, authentication.token);
    }

    removeAuthentication(): void {
        localStorage.removeItem(TOKEN_KEY);
    }

    hasValidAuthentication(): boolean {
        return this.isValidAuthentication(this.getAuthentication());
    }

    isValidAuthentication = (authentication: Authentication | undefined): boolean => {
        if (!authentication) {
            return false;
        }

        if (authentication.token === null) {
            return false;
        }

        if (authentication.token === undefined) {
            return false;
        }

        if (authentication.token === '') {
            return false;
        }

        return true;
    }

}