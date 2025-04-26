import {Inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';


@Injectable({
  providedIn: 'root'
})

export class FeedbackSuggestionService {

  private urlFeedBackSuggestion = '';

  constructor(private http : HttpClient) {
  }

  createSuggestion(suggestion: string) : Observable<any> {
    console.log(suggestion);
    return this.http.post(this.urlFeedBackSuggestion, suggestion);
  }


}
