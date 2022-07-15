import { Component, Input } from '@angular/core';
import { Comment } from '@backbase/ac-common-ang';

@Component({
  selector: 'bb-comments',
  templateUrl: './comments.component.html',
})
export class CommentsComponent {
  @Input() comments: Comment[] = [];
}
