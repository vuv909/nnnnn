import { createReducer, on } from '@ngrx/store';
import { storeUser } from './login.action';

const initialState: any = null;

const _storeUserReducer = createReducer(
  initialState,
  on(storeUser, (state, action) => ({
    ...action.user,
  }))
);

export function storeUserReducer(state: any, action: any) {
  return _storeUserReducer(state, action);
}
