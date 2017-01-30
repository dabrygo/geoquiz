package geoquiz;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Model {
    public static final Continent DEFAULT_CONTINENT = Continent.NORTH_AMERICA;

    enum AnswerState {
        CORRECT, INCORRECT, UNKNOWN
    };

    List<IQuizCountry> masterList;
    List<IQuizCountry> quizCountries;
    int index;
    AnswerState[] answerStates;
    private IRegionFactory regionFactory;
    boolean showName;
    boolean showCapital;
    boolean showFlag;

    public Model(IRegionFactory regionFactory) {
        masterList = regionFactory.regionFrom(Continent.WORLD);
        this.regionFactory = regionFactory;
        changeQuizCountries(DEFAULT_CONTINENT);
        answerStates = new AnswerState[quizCountries.size()];
        for (int i = 0; i < answerStates.length; i++) {
            answerStates[i] = AnswerState.UNKNOWN;
        }
        
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(Paths.get("rsc", "geoquiz", "settings.properties").toFile()));
            showName = properties.get("name").equals("true");
            showCapital = properties.get("capital").equals("true");
            showFlag = properties.get("flag").equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IQuizCountry currentCountry() {
        if (index >= quizCountries.size()) {
            return new NullCountry();
        }
        return quizCountries.get(index);
    }

    public IQuizCountry nextCountry(AnswerState answerState) {
        if (moreQuestionsInQuiz()) {
            answerStates[index] = answerState;
            index++;
        }
        return currentCountry();
    }

    boolean moreQuestionsInQuiz() {
        return index < quizCountries.size();
    }

    public IQuizCountry previousCountry() {
        if (index > 0) {
            index--;
        }
        return currentCountry();
    }

    public void changeQuizCountries(Continent regionCode) {
        Region continent = regionFactory.regionFrom(regionCode);
        quizCountries = continent;
        Collections.shuffle(quizCountries);
        index = 0;
        answerStates = new AnswerState[quizCountries.size()];
        for (int i = 0; i < answerStates.length; i++) {
            answerStates[i] = AnswerState.UNKNOWN;
        }
    }

    public void setAnswerState(AnswerState answerState) {
        answerStates[index] = answerState;
    }

    public AnswerState getAnswerState() {
        return answerStates[index];
    }

    public int getCorrectTally() {
        return (int) Arrays.stream(answerStates).filter(state -> state.equals(AnswerState.CORRECT)).count();
    }

    public int getIncorrectTally() {
        return (int) Arrays.stream(answerStates).filter(state -> state.equals(AnswerState.INCORRECT)).count();
    }
}
