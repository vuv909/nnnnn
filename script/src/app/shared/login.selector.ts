import { createFeatureSelector, createSelector } from '@ngrx/store';

const getUserState = createFeatureSelector('storeUser');

export const getUser = createSelector(getUserState, (state: any) => {
  return state;
});


