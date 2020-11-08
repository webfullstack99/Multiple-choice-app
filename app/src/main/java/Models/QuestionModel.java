package Models;

import android.app.Activity;
import android.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import Defines.ICallback;
import Defines.Question;

public class QuestionModel extends Model {

    ICallback<Question> iCallback;

    public QuestionModel(Activity activity, Fragment fragment) {
        super("questions", activity);
        iCallback = (ICallback<Question>) fragment;
    }

    @Override
    public void listAll(final HashMap<String, String> params, final String tag) {
        db.collection(collection)
                .whereIn("level", Arrays.asList(params.get("level")))
                .orderBy("created", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Question> questionArrayList = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Question qt = Question.getQuestionBySnapshot(queryDocumentSnapshot);
                    questionArrayList.add(qt);
                }
                iCallback.listCallBack(questionArrayList, tag);
            }
        });
    }

    public void updateItem(final Question question, final String tag) {
        db.collection(collection).document(question.getId()).update(question.getDocData()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                iCallback.itemCallBack(question, tag);
            }
        });
    }

    public void addItem(final Question question, final String tag) {
        Map<String, Object> docData = question.getDocData();
        db.collection(collection).add(question.getDocData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                question.setId(documentReference.getId());
                iCallback.itemCallBack(question, tag);
            }
        });
    }

    public void deleteItemById(final String id, final String tag) {
        db.collection(collection).document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                iCallback.itemCallBack(null, tag);
            }
        });
    }


    @Override
    public void getItemById(String id, final String tag) {
        db.collection(collection).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Question question = Question.getQuestionBySnapshot(documentSnapshot);
                iCallback.itemCallBack(question, tag);
            }
        });
    }

    public void pushFakeData() {
        ArrayList<Question> questionArrayList = new ArrayList<>();
        questionArrayList.add(new Question(null,
                "hey didn't reach an agreement ______ their differences.", "B",
                "on account of", "due", "because", "owning", "text", "hard", -1));
        questionArrayList.add(new Question(null,
                "I'm very happy _____ in India. I really miss being there.", "A",
                "to live", "to have lived", "to be lived", "to be living", "text", "hard", -1));
        questionArrayList.add(new Question(null,
                "It is verry important for you... get well", "D",
                "for", "with", "in", "to", "text", "easy", -1));
        questionArrayList.add(new Question(null,
                "We have two ..... We see with them.", "A",
                "eyes", "legs", "hands", "ears", "text", "easy", -1));
        questionArrayList.add(new Question(null,
                "I am on my.... to the airport.", "B",
                "book", "street", "way", "tree", "text", "easy", -1));
        questionArrayList.add(new Question(null,
                "I am afraid ... studying English", "B",
                "to", "of", "for", "with", "text", "easy", -1));
        for (Question question : questionArrayList) {
            db.collection(collection).add(question.getDocData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                }
            });
        }
    }
}
