import { createAction, props } from '@ngrx/store';

export const storeUser = createAction(
  '[User] Store User',
  props<{ user: any }>()
);
