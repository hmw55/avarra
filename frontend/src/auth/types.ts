export type RegisterRequest = {
  username: string
  password: string
  email?: string
}

export type RegisterResponse = {
  userId: string
  username: string
  recoveryCode: string
}

export type LoginRequest = {
  username: string
  password: string
}

export type LoginResponse = {
  username: string
}

export type CurrentUser = {
  username: string
}