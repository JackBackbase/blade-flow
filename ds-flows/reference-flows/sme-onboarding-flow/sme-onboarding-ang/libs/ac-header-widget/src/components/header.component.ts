import { Component, Input } from '@angular/core';

@Component({
  selector: 'bb-header',
  templateUrl: 'header.component.html',
})
export class HeaderComponent {
  @Input() userName!: string;
  @Input() subtitle!: string;
}
