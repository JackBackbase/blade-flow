import { TemplateRef } from '@angular/core';

export type NotificationMessage = string | TemplateRef<any>;
export type NotificationModifier = 'success' | 'info' | 'warning' | 'error' | undefined;
