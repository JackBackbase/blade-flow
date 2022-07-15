import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { ApplicationCenterStoreEffects } from './services/store/effects';
import { reducer as applicationCenterReducer } from './services/store/reducer';
import { RequestStatusBadgeModule } from './request-status-badge/request-status-badge.module';
import { SuspenseModule } from './suspense/suspense.module';

@NgModule({
  imports: [
    CommonModule,
    StoreModule.forRoot({}),
    EffectsModule.forRoot([]),
    StoreModule.forFeature('applicationCenter', applicationCenterReducer),
    EffectsModule.forFeature([ApplicationCenterStoreEffects]),
  ],
  providers: [FlowInteractionService],
  declarations: [],
  exports: [SuspenseModule, RequestStatusBadgeModule],
})
export class AcCommonAngModule {}
