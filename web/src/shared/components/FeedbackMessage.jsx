export default function FeedbackMessage({ feedback, className = "", ...props }) {
  if (!feedback?.message) {
    return null;
  }

  const type = feedback.type || "success";

  return (
    <div className={`feedback ${type} ${className}`.trim()} role="status" {...props}>
      {feedback.message}
    </div>
  );
}
