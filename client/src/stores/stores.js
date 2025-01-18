import { writable } from 'svelte/store';

export const accounts = writable([]);
export const isAuthenticated = writable(false);

export const transactions = writable([]);
export const triggerFetchTransactions = writable(false);

export async function checkLoginStatus() {
    try {
        const response = await fetch('/api/check-login', {
            method: 'GET',
            credentials: 'include',
        });
        isAuthenticated.set(response.ok);
        return response.ok;
    } catch (err) {
        isAuthenticated.set(false);
        return false;
    }
}
