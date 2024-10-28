import api from "./api";

//Login
export const login = (username, password) => api.post(`/login?username=${username}&password=${password}`)

// User Info
export const info = () => api.get(`/users/info`)

// Sign Up
export const join = (data) => api.post(`/users`, data)

// Update User Info
export const update = (data) => api.put(`/users`, data)

// Remove
export const remove = (userId) => api.delete(`/users/${userId}`)


