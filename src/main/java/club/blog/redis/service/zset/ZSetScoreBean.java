package club.blog.redis.service.zset;

public class ZSetScoreBean implements  Comparable{

    private String value;

    private Long  score;

    @Override
    public int compareTo(Object o) {
        if(o instanceof  ZSetScoreBean){
            ZSetScoreBean  scoreBean = (ZSetScoreBean) o;
            return (this.score - scoreBean.score>0)?1:0;
        }
        return 0;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public ZSetScoreBean(String value, Long score) {
        this.value = value;
        this.score = score;
    }
}
