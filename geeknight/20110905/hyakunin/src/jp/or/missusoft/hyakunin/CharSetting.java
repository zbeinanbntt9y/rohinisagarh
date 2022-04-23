package jp.or.missusoft.hyakunin;

public class CharSetting {
    /**
     * ����
     */
    public final String charcter;

    /**
     * ��]�p�x
     */
    public final float angle;

    /**
     * x�̍���<br />
     * Paint#getFontSpacing() * x���������<br />
     * -0.5f���ݒ肳�ꂽ�ꍇ�A1/2���������ɂ����
     */
    public final float x;

    /**
     * x�̍���<br />
     * Paint#getFontSpacing() * y���������<br />
     * -0.5f���ݒ肳�ꂽ�ꍇ�A1/2��������ɂ����
     */
    public final float y;

    public CharSetting(String charcter, float angle, float x, float y) {
        super();
        this.charcter = charcter;
        this.angle = angle;
        this.x = x;
        this.y = y;
    }

    public static final CharSetting[] settings = {
            new CharSetting("�A", 0.0f, 0.7f, -0.6f), new CharSetting("�B", 0.0f, 0.7f, -0.6f),
            new CharSetting("�u", 90.0f, -1.0f, -0.3f), new CharSetting("�v", 90.0f, -1.0f, 0.0f),
            new CharSetting("��", 0.0f, 0.1f, -0.1f), new CharSetting("��", 0.0f, 0.1f, -0.1f),
            new CharSetting("��", 0.0f, 0.1f, -0.1f), new CharSetting("��", 0.0f, 0.1f, -0.1f),
            new CharSetting("��", 0.0f, 0.1f, -0.1f), new CharSetting("��", 0.0f, 0.1f, -0.1f),
            new CharSetting("��", 0.0f, 0.1f, -0.1f), new CharSetting("��", 0.0f, 0.1f, -0.1f),
            new CharSetting("��", 0.0f, 0.1f, -0.1f), new CharSetting("�@", 0.0f, 0.1f, -0.1f),
            new CharSetting("�B", 0.0f, 0.1f, -0.1f), new CharSetting("�D", 0.0f, 0.1f, -0.1f),
            new CharSetting("�F", 0.0f, 0.1f, -0.1f), new CharSetting("�H", 0.0f, 0.1f, -0.1f),
            new CharSetting("�b", 0.0f, 0.1f, -0.1f), new CharSetting("��", 0.0f, 0.1f, -0.1f),
            new CharSetting("��", 0.0f, 0.1f, -0.1f), new CharSetting("��", 0.0f, 0.1f, -0.1f),
            new CharSetting("�[", -90.0f, 0.0f, 0.9f), new CharSetting("a", 90.0f, 0.0f, -0.1f),
            new CharSetting("b", 90.0f, 0.0f, -0.1f), new CharSetting("c", 90.0f, 0.0f, -0.1f),
            new CharSetting("d", 90.0f, 0.0f, -0.1f), new CharSetting("e", 90.0f, 0.0f, -0.1f),
            new CharSetting("f", 90.0f, 0.0f, -0.1f), new CharSetting("g", 90.0f, 0.0f, -0.1f),
            new CharSetting("h", 90.0f, 0.0f, -0.1f), new CharSetting("i", 90.0f, 0.0f, -0.1f),
            new CharSetting("j", 90.0f, 0.0f, -0.1f), new CharSetting("k", 90.0f, 0.0f, -0.1f),
            new CharSetting("l", 90.0f, 0.0f, -0.1f), new CharSetting("m", 90.0f, 0.0f, -0.1f),
            new CharSetting("n", 90.0f, 0.0f, -0.1f), new CharSetting("o", 90.0f, 0.0f, -0.1f),
            new CharSetting("p", 90.0f, 0.0f, -0.1f), new CharSetting("q", 90.0f, 0.0f, -0.1f),
            new CharSetting("r", 90.0f, 0.0f, -0.1f), new CharSetting("s", 90.0f, 0.0f, -0.1f),
            new CharSetting("t", 90.0f, 0.0f, -0.1f), new CharSetting("u", 90.0f, 0.0f, -0.1f),
            new CharSetting("v", 90.0f, 0.0f, -0.1f), new CharSetting("w", 90.0f, 0.0f, -0.1f),
            new CharSetting("x", 90.0f, 0.0f, -0.1f), new CharSetting("y", 90.0f, 0.0f, -0.1f),
            new CharSetting("z", 90.0f, 0.0f, -0.1f), new CharSetting("A", 90.0f, 0.0f, -0.1f),
            new CharSetting("B", 90.0f, 0.0f, -0.1f), new CharSetting("C", 90.0f, 0.0f, -0.1f),
            new CharSetting("D", 90.0f, 0.0f, -0.1f), new CharSetting("E", 90.0f, 0.0f, -0.1f),
            new CharSetting("F", 90.0f, 0.0f, -0.1f), new CharSetting("G", 90.0f, 0.0f, -0.1f),
            new CharSetting("H", 90.0f, 0.0f, -0.1f), new CharSetting("I", 90.0f, 0.0f, -0.1f),
            new CharSetting("J", 90.0f, 0.0f, -0.1f), new CharSetting("K", 90.0f, 0.0f, -0.1f),
            new CharSetting("L", 90.0f, 0.0f, -0.1f), new CharSetting("M", 90.0f, 0.0f, -0.1f),
            new CharSetting("N", 90.0f, 0.0f, -0.1f), new CharSetting("O", 90.0f, 0.0f, -0.1f),
            new CharSetting("P", 90.0f, 0.0f, -0.1f), new CharSetting("Q", 90.0f, 0.0f, -0.1f),
            new CharSetting("R", 90.0f, 0.0f, -0.1f), new CharSetting("S", 90.0f, 0.0f, -0.1f),
            new CharSetting("T", 90.0f, 0.0f, -0.1f), new CharSetting("U", 90.0f, 0.0f, -0.1f),
            new CharSetting("V", 90.0f, 0.0f, -0.1f), new CharSetting("W", 90.0f, 0.0f, -0.1f),
            new CharSetting("X", 90.0f, 0.0f, -0.1f), new CharSetting("Y", 90.0f, 0.0f, -0.1f),
            new CharSetting("Z", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("��", 90.0f, 0.0f, -0.1f),
            new CharSetting("��", 90.0f, 0.0f, -0.1f), new CharSetting("�`", 90.0f, 0.0f, -0.1f),
            new CharSetting("�a", 90.0f, 0.0f, -0.1f), new CharSetting("�b", 90.0f, 0.0f, -0.1f),
            new CharSetting("�c", 90.0f, 0.0f, -0.1f), new CharSetting("�d", 90.0f, 0.0f, -0.1f),
            new CharSetting("�e", 90.0f, 0.0f, -0.1f), new CharSetting("�f", 90.0f, 0.0f, -0.1f),
            new CharSetting("�g", 90.0f, 0.0f, -0.1f), new CharSetting("�h", 90.0f, 0.0f, -0.1f),
            new CharSetting("�i", 90.0f, 0.0f, -0.1f), new CharSetting("�j", 90.0f, 0.0f, -0.1f),
            new CharSetting("�k", 90.0f, 0.0f, -0.1f), new CharSetting("�l", 90.0f, 0.0f, -0.1f),
            new CharSetting("�m", 90.0f, 0.0f, -0.1f), new CharSetting("�n", 90.0f, 0.0f, -0.1f),
            new CharSetting("�o", 90.0f, 0.0f, -0.1f), new CharSetting("�p", 90.0f, 0.0f, -0.1f),
            new CharSetting("�q", 90.0f, 0.0f, -0.1f), new CharSetting("�r", 90.0f, 0.0f, -0.1f),
            new CharSetting("�s", 90.0f, 0.0f, -0.1f), new CharSetting("�t", 90.0f, 0.0f, -0.1f),
            new CharSetting("�u", 90.0f, 0.0f, -0.1f), new CharSetting("�v", 90.0f, 0.0f, -0.1f),
            new CharSetting("�w", 90.0f, 0.0f, -0.1f), new CharSetting("�x", 90.0f, 0.0f, -0.1f),
            new CharSetting("�y", 90.0f, 0.0f, -0.1f), new CharSetting("�F", 90.0f, 0.0f, -0.1f),
            new CharSetting("�G", 90.0f, 0.0f, -0.1f), new CharSetting("�^", 90.0f, 0.0f, -0.1f),
            new CharSetting("", 90.0f, 0.0f, -0.1f), new CharSetting(":", 90.0f, 0.0f, -0.1f),
            new CharSetting(";", 90.0f, 0.0f, -0.1f), new CharSetting("/", 90.0f, 0.0f, -0.1f),
            new CharSetting(".", 90.0f, 0.0f, -0.1f),
    };

    public static CharSetting getSetting(String character) {
        for (int i = 0; i < settings.length; i++) {
            if (settings[i].charcter.equals(character)) {
                return settings[i];
            }
        }
        return null;
    }

    private static final String[] PUNCTUATION_MARK = {
            "�A", "�B", "�u", "�v"
    };

    public static boolean isPunctuationMark(String s) {
        for (String functuantionMark : PUNCTUATION_MARK) {
            if (functuantionMark.equals(s)) {
                return true;
            }
        }
        return false;
    }

}