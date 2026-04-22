import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-selector',
  imports: [FormsModule],
  templateUrl: './selector.html',
  styleUrl: './selector.css',
})
export class Selector {
  @Input({required: true}) options!: string[];
  @Output() confirm = new EventEmitter<string>();

  protected selectedValue: string = '';
}
