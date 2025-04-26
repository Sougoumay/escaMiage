import {Inject, Injectable} from '@angular/core';
import {FeedbackSuggestionService} from '../service/feedback.suggestion.service';
import {Observable} from 'rxjs';


@Injectable({
  providedIn: 'root'
})

export class FeedbackSuggestionRepository {

  constructor(private feedbackSuggestionService: FeedbackSuggestionService) {
  }

  createSuggestion(suggestion: string) : Observable<any> {
    return this.feedbackSuggestionService.createSuggestion(suggestion);
  }


}
